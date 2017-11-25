package com.zhh.hyman.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 */

public class MovieItem implements Parcelable {
    public long id;

    // 电影名称
    public String title;

    // 电影海报
    public String poster_path;

    // 剧情简介
    public String overview;

    // 用户评分
    public double vote_average;

    // 发布日期
    public String release_date;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.title);
        dest.writeString(this.poster_path);
        dest.writeString(this.overview);
        dest.writeDouble(this.vote_average);
        dest.writeString(this.release_date);
    }

    public MovieItem() {
    }

    protected MovieItem(Parcel in) {
        this.id = in.readLong();
        this.title = in.readString();
        this.poster_path = in.readString();
        this.overview = in.readString();
        this.vote_average = in.readDouble();
        this.release_date = in.readString();
    }

    public static final Parcelable.Creator<MovieItem> CREATOR = new Parcelable.Creator<MovieItem>() {
        @Override
        public MovieItem createFromParcel(Parcel source) {
            return new MovieItem(source);
        }

        @Override
        public MovieItem[] newArray(int size) {
            return new MovieItem[size];
        }
    };
}
