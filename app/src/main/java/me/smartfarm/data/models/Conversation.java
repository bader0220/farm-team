package me.smartfarm.data.models;


import java.util.ArrayList;
import java.util.List;

public class Conversation {
    private List<String> users = new ArrayList<>();

    private long updateTime;

    public Conversation() {
    }

    public Conversation(List<String> users, long updateTime) {
        this.users = users;
        this.updateTime = updateTime;
    }


    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}
