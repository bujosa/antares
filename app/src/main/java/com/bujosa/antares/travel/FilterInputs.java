package com.bujosa.antares.travel;

public class FilterInputs {

    private Boolean favorite = null;
    private double minPrice = -1;
    private double maxPrice = -1;

    public FilterInputs() {
    }

    public boolean hasFavorite() {
        return favorite != null;
    }

    public boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public boolean hasMinPrice() {
        return (minPrice > 0);
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public boolean hasMaxPrice() {
        return (maxPrice > 0);
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }
}