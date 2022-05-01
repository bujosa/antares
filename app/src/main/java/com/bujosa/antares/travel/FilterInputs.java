package com.bujosa.antares.travel;

public class FilterInputs {

    private Boolean favorite = null;
    private Double minPrice = Double.NEGATIVE_INFINITY;
    private Double maxPrice = Double.NEGATIVE_INFINITY;

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

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public boolean hasMaxPrice() {
        return (maxPrice > 0);
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }
}