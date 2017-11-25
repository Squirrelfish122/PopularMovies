package com.zhh.hyman.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zhh.hyman.popularmovies.data.MovieItem;
import com.zhh.hyman.popularmovies.utils.DensityTool;
import com.zhh.hyman.popularmovies.utils.NetWorkUtils;

import java.util.ArrayList;

/**
 *
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();
    private static final int PADDING_ITEM = 5;

    private Context mContext;
    private ArrayList<MovieItem> movieItemArrayList;

    OnListItemClickListener onListItemClickListener;

    private int itemWidth;
    private int itemHeight;


    public MovieAdapter(Context context, int itemWidth, OnListItemClickListener onListItemClickListener) {
        this.mContext = context;
        this.itemWidth = itemWidth;
        if (itemWidth > 0) {
            itemHeight = getItemHeight();
        }
        Log.i(TAG, "itemWidth = " + itemWidth + ", itemHeight = " + itemHeight);
        this.onListItemClickListener = onListItemClickListener;
    }

    private int getItemHeight() {
        int boundWidth = DensityTool.dp2px(mContext, PADDING_ITEM) * 2;
        int imageWidth = itemWidth - boundWidth;
        int imageHeight = (int) (imageWidth * 278f / 185);
        return imageHeight + boundWidth;
    }

    public void bindData(ArrayList<MovieItem> movieItemArrayList) {
        this.movieItemArrayList = movieItemArrayList;
        this.notifyDataSetChanged();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.recycler_item_movie, parent, false);
        ViewGroup.LayoutParams layoutParams = inflate.getLayoutParams();
        layoutParams.width = itemWidth;
        layoutParams.height = itemHeight;
        inflate.setLayoutParams(layoutParams);
        return new MovieViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.showView(movieItemArrayList.get(position).poster_path);
    }

    @Override
    public int getItemCount() {
        return movieItemArrayList == null ? 0 : movieItemArrayList.size();
    }


    public interface OnListItemClickListener {
        void onItemClick(MovieItem movieItem);
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView posterView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            posterView = (ImageView) itemView.findViewById(R.id.item_movie_poster);
            itemView.setOnClickListener(this);
        }

        public void showView(String posterRelativePath) {
            NetWorkUtils.loadImage(posterView, posterRelativePath);
        }

        @Override
        public void onClick(View view) {
            if (onListItemClickListener != null) {
                int adapterPosition = getAdapterPosition();
                onListItemClickListener.onItemClick(movieItemArrayList.get(adapterPosition));
            }
        }
    }
}
