package me.smartfarm.data.models;

import java.util.ArrayList;
import java.util.List;

public class Reservation {
    private static Reservation instance;
    private String ownerId;
    private String farmId;
    private long creationDate;
    private double amount;
    private String traderId;
    private double reservationPrice;

    private Reservation() {
    }

    public static Reservation getInstance() {
        if (instance == null) {
            instance = new Reservation();
        }
        return instance;
    }

    public Reservation(String ownerId, String farmId, long creationDate, double amount, String traderId) {
        this.ownerId = ownerId;
        this.farmId = farmId;
        this.creationDate = creationDate;
        this.amount = amount;
        this.traderId = traderId;
    }

    public Reservation(String ownerId, String farmId, long creationDate, double amount, String traderId, double reservationPrice) {
        this.ownerId = ownerId;
        this.farmId = farmId;
        this.creationDate = creationDate;
        this.amount = amount;
        this.traderId = traderId;
        this.reservationPrice = reservationPrice;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTraderId() {
        return traderId;
    }

    public void setTraderId(String traderId) {
        this.traderId = traderId;
    }

    public double getReservationPrice() {
        return reservationPrice;
    }

    public void setReservationPrice(double reservationPrice) {
        this.reservationPrice = reservationPrice;
    }
}
