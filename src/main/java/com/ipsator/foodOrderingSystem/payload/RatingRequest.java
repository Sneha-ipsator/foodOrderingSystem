package com.ipsator.foodOrderingSystem.payload;

public class RatingRequest {
    public int getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(int ratingValue) {
        this.ratingValue = ratingValue;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    private int ratingValue;
    private String feedback;


}
