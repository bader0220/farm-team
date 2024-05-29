package me.smartfarm.data.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Farm implements Serializable {
    private String title;
    private List<String> images = new ArrayList<>();
    private String description;
    private String harvestDateFrom;
    private String harvestDateTo;
    private int corpTypeId;
    private double unitPrice;
    private long creationDate;
    private double totalAmount;
    private double availableAmount;
    private String ownerId;
    private List<MapPoint> area = new ArrayList<>();
    private List<Reservation> reservations = new ArrayList<>();
    private int cityId;
    private int neighborhoodId;

    public Farm() {
    }

    public Farm(String title, List<String> images, String description, String harvestDateFrom, String harvestDateTo, int corpTypeId, double unitPrice, long creationDate, double totalAmount, double availableAmount, String ownerId, List<MapPoint> area, List<Reservation> reservations, int cityId, int neighborhoodId) {
        this.title = title;
        this.images = images;
        this.description = description;
        this.harvestDateFrom = harvestDateFrom;
        this.harvestDateTo = harvestDateTo;
        this.corpTypeId = corpTypeId;
        this.unitPrice = unitPrice;
        this.creationDate = creationDate;
        this.totalAmount = totalAmount;
        this.availableAmount = availableAmount;
        this.ownerId = ownerId;
        this.area = area;
        this.reservations = reservations;
        this.cityId = cityId;
        this.neighborhoodId = neighborhoodId;
    }

    public Farm(String title, List<String> images, String description, String harvestDateFrom, String harvestDateTo, int corpTypeId, double unitPrice, long creationDate, double availableAmount, String ownerId, List<MapPoint> area, int cityId, int neighborhoodId) {
        this.title = title;
        this.images = images;
        this.description = description;
        this.harvestDateFrom = harvestDateFrom;
        this.harvestDateTo = harvestDateTo;
        this.corpTypeId = corpTypeId;
        this.unitPrice = unitPrice;
        this.creationDate = creationDate;
        this.availableAmount = availableAmount;
        this.ownerId = ownerId;
        this.area = area;
        this.cityId = cityId;
        this.neighborhoodId = neighborhoodId;
    }

    public Farm(String title, List<String> images, String description, String harvestDateFrom, String harvestDateTo, int corpTypeId, double unitPrice, long creationDate, double availableAmount, String ownerId, List<MapPoint> area, List<Reservation> reservations, int cityId, int neighborhoodId) {
        this.title = title;
        this.images = images;
        this.description = description;
        this.harvestDateFrom = harvestDateFrom;
        this.harvestDateTo = harvestDateTo;
        this.corpTypeId = corpTypeId;
        this.unitPrice = unitPrice;
        this.creationDate = creationDate;
        this.availableAmount = availableAmount;
        this.ownerId = ownerId;
        this.area = area;
        this.reservations = reservations;
        this.cityId = cityId;
        this.neighborhoodId = neighborhoodId;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHarvestDateFrom() {
        return harvestDateFrom;
    }

    public void setHarvestDateFrom(String harvestDateFrom) {
        this.harvestDateFrom = harvestDateFrom;
    }

    public String getHarvestDateTo() {
        return harvestDateTo;
    }

    public void setHarvestDateTo(String harvestDateTo) {
        this.harvestDateTo = harvestDateTo;
    }

    public int getCorpTypeId() {
        return corpTypeId;
    }

    public void setCorpTypeId(int corpTypeId) {
        this.corpTypeId = corpTypeId;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public double getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(double availableAmount) {
        this.availableAmount = availableAmount;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public List<MapPoint> getArea() {
        return area;
    }

    public void setArea(List<MapPoint> area) {
        this.area = area;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getNeighborhoodId() {
        return neighborhoodId;
    }

    public void setNeighborhoodId(int neighborhoodId) {
        this.neighborhoodId = neighborhoodId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}


