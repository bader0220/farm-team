package me.smartfarm.data.repositories;

public interface FirebaseTask<T> {
    public void onComplete(T taskResult);
}
