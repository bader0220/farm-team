package me.smartfarm.data.repositories;

public interface Callback {
    public void taskSuccess(Object o);

    public void taskFailed(Exception e);
}
