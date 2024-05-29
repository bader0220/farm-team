package me.smartfarm.data.models;

import java.util.ArrayList;
import java.util.List;

public class User {
    private static User instance;
    private String id;
    private String firstName;
    private String lastName;

    private String image;
    private String email;
    private String bio;
    private String phone;

    private int residentCityId;
    private int residentNeighborhoodId;
    private int userTypeId;
    private double balance;

    private List<String> chats = new ArrayList<>();
    private String password;

    private User() {
    }


    public synchronized static User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }



    public User(String id, String firstName, String lastName, String image, String email, String bio, String phone, int residentCityId, int residentNeighborhoodId, int userTypeId, double balance, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.image = image;
        this.email = email;
        this.bio = bio;
        this.phone = phone;
        this.residentCityId = residentCityId;
        this.residentNeighborhoodId = residentNeighborhoodId;
        this.userTypeId = userTypeId;
        this.balance = balance;
        this.password = password;
    }

    public User(String id, String firstName, String lastName, String image, String email, String bio, String phone, int residentCityId, int residentNeighborhoodId, int userTypeId, double balance, List<String> chats, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.image = image;
        this.email = email;
        this.bio = bio;
        this.phone = phone;
        this.residentCityId = residentCityId;
        this.residentNeighborhoodId = residentNeighborhoodId;
        this.userTypeId = userTypeId;
        this.balance = balance;
        this.chats = chats;
        this.password = password;
    }

    public static void setInstance(User instance) {
        User.instance = instance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getResidentCityId() {
        return residentCityId;
    }

    public void setResidentCityId(int residentCityId) {
        this.residentCityId = residentCityId;
    }

    public int getResidentNeighborhoodId() {
        return residentNeighborhoodId;
    }

    public void setResidentNeighborhoodId(int residentNeighborhoodId) {
        this.residentNeighborhoodId = residentNeighborhoodId;
    }

    public int getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(int userTypeId) {
        this.userTypeId = userTypeId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public List<String> getChats() {
        return chats;
    }
    public void addChat(String chatId) {
        chats.add(chatId);
    }
    public void removeChat(String chatId) {
        chats.remove(chatId);
    }
    public void clearChats() {
        chats.clear();
    }

}
