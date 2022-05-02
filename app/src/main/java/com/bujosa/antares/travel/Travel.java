package com.bujosa.antares.travel;

import com.google.firebase.firestore.DocumentId;
import java.io.Serializable;
import java.util.Date;

public class Travel implements Serializable {

    @DocumentId
    private String id;

    private String title;

    private String description;

    private Date startDate;

    private Date endDate;

    private Float price;

    private Boolean favorite = false;

    private String image;

    private double latitude;

    private double longitude;

    public Travel() {
    }

    public String getId() {
        return id;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean validate() {
        return (title != null && !title.isEmpty()) &&
                (description != null && !description.isEmpty()) &&
                (price != 0) && (startDate != null) && (endDate != null);
    }
}
