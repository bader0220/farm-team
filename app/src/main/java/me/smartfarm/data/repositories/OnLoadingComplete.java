package me.smartfarm.data.repositories;

public interface OnLoadingComplete<T> {
    void onLoaded(T object);
}
