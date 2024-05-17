package me.smartfarm.data.models;

import java.util.Date;
import java.util.List;

public class Farm {
    private String title;
    private List<List<String>> images;
    private String description;
    private Date harvestDateFrom;
    private Date harvestDateTo;
    private String corpType;
    private String uom;
    private long creationDate;
    private User owner;
    private double availableAmount;
    private List<MapPoint> area;
    private String city;
    private String neighborhood;

    public Farm() {
    }

    public Farm(String title, List<List<String>> images, String description, Date harvestDateFrom, Date harvestDateTo, String corpType, String uom, long creationDate, User owner, double availableAmount, List<MapPoint> area, String city, String neighborhood) {
        this.title = title;
        this.images = images;
        this.description = description;
        this.harvestDateFrom = harvestDateFrom;
        this.harvestDateTo = harvestDateTo;
        this.corpType = corpType;
        this.uom = uom;
        this.creationDate = creationDate;
        this.owner = owner;
        this.availableAmount = availableAmount;
        this.area = area;
        this.city = city;
        this.neighborhood = neighborhood;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<List<String>> getImages() {
        return images;
    }

    public void setImages(List<List<String>> images) {
        this.images = images;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getHarvestDateFrom() {
        return harvestDateFrom;
    }

    public void setHarvestDateFrom(Date harvestDateFrom) {
        this.harvestDateFrom = harvestDateFrom;
    }

    public Date getHarvestDateTo() {
        return harvestDateTo;
    }

    public void setHarvestDateTo(Date harvestDateTo) {
        this.harvestDateTo = harvestDateTo;
    }

    public String getCorpType() {
        return corpType;
    }

    public void setCorpType(String corpType) {
        this.corpType = corpType;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public double getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(double availableAmount) {
        this.availableAmount = availableAmount;
    }

    public List<MapPoint> getArea() {
        return area;
    }

    public void setArea(List<MapPoint> area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }


}
