package com.zhh.hyman.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.zhh.hyman.popularmovies.data.MovieItem;
import com.zhh.hyman.popularmovies.data.MovieListResult;
import com.zhh.hyman.popularmovies.listener.LoadingDataListener;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements MovieAdapter.OnListItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int NUM_COLUMN = 3;

    private RecyclerView recyclerView;
    private Button retryButton;
    private ProgressBar progressBar;

    private MovieAdapter movieAdapter;
    private GridLayoutManager gridLayoutManager;

    private int asyncTaskType = GetMovieListAsyncTask.ASYNC_TASK_TYPE_GET_POPULAR_MOVIES;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);
        retryButton = (Button) findViewById(R.id.main_btn_retry);
        progressBar = (ProgressBar) findViewById(R.id.main_pb_indicator);

        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData();
            }
        });

        gridLayoutManager = new GridLayoutManager(getApplicationContext(), NUM_COLUMN);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setHasFixedSize(true);

        movieAdapter = new MovieAdapter(getApplicationContext(), getItemWidth(), this);
        recyclerView.setAdapter(movieAdapter);

        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (getMovieListAsyncTask != null) {
            getMovieListAsyncTask.cancel(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.main_menu_item_popular:
                if (asyncTaskType != GetMovieListAsyncTask.ASYNC_TASK_TYPE_GET_POPULAR_MOVIES) {
                    asyncTaskType = GetMovieListAsyncTask.ASYNC_TASK_TYPE_GET_POPULAR_MOVIES;
                    loadData();
                } else {
                    showTip(R.string.menu_item_action_popular);
                }
                break;

            case R.id.main_menu_item_rated:
                if (asyncTaskType != GetMovieListAsyncTask.ASYNC_TASK_TYPE_GET_RATED_MOVIES) {
                    asyncTaskType = GetMovieListAsyncTask.ASYNC_TASK_TYPE_GET_RATED_MOVIES;
                    loadData();
                } else {
                    showTip(R.string.menu_item_action_rated);
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showTip(int tipId) {
        String tip = getString(tipId);
        Toast.makeText(getApplicationContext(), "当前已经" + tip, Toast.LENGTH_SHORT).show();
    }

    private int getItemWidth() {
        WindowManager systemService = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        systemService.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels / NUM_COLUMN;
    }

    @Override
    public void onItemClick(MovieItem movieItem) {
        // 进入详情页
        //        Toast.makeText(getApplicationContext(), "准备进入电影：" + movieId, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.PARAM_KEY_MOVIE_ITEM, movieItem);
        startActivity(intent);
    }

    GetMovieListAsyncTask getMovieListAsyncTask;

    private void loadData() {
        if (getMovieListAsyncTask != null) {
            getMovieListAsyncTask.cancel(false);
        }
        getMovieListAsyncTask = new GetMovieListAsyncTask(new LoadingDataListener<MovieListResult>() {
            @Override
            public void preExecute() {
                showLoadingView();
            }

            @Override
            public void postExecute(MovieListResult movieListResult) {
                showContentView(movieListResult);
            }
        }, asyncTaskType);
        getMovieListAsyncTask.execute();
    }


    private void showLoadingView() {
        recyclerView.setVisibility(View.INVISIBLE);
        retryButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void showLoadFailedView() {
        recyclerView.setVisibility(View.INVISIBLE);
        retryButton.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void showContentView(MovieListResult movieListResult) {
        if (movieListResult == null) {
            showLoadFailedView();
            return;
        }

        ArrayList<MovieItem> movieItems = new ArrayList<>(Arrays.asList(movieListResult.results));
        if (movieItems.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            retryButton.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            movieAdapter.bindData(movieItems);
        } else {
            showLoadFailedView();
        }
    }
}
