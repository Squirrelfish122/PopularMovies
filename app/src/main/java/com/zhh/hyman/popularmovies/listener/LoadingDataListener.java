package com.zhh.hyman.popularmovies.listener;

/**
 *
 */

public interface LoadingDataListener<T> {

    void preExecute();
    void postExecute(T t);

}
