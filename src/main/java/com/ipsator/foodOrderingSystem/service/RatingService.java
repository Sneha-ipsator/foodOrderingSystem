package com.ipsator.foodOrderingSystem.service;

import com.ipsator.foodOrderingSystem.entity.Rating;
import com.ipsator.foodOrderingSystem.payload.RatingRequest;

public interface RatingService {
    Rating submitRating(Long restaurantId, RatingRequest ratingRequest, String user);

}
