package com.ipsator.foodOrderingSystem.service;

import com.ipsator.foodOrderingSystem.entity.Order;
import com.ipsator.foodOrderingSystem.entity.Rating;
import com.ipsator.foodOrderingSystem.entity.Restaurant;
import com.ipsator.foodOrderingSystem.entity.User;
import com.ipsator.foodOrderingSystem.payload.RatingRequest;
import com.ipsator.foodOrderingSystem.repository.OrderRepository;
import com.ipsator.foodOrderingSystem.repository.RatingRepository;
import com.ipsator.foodOrderingSystem.repository.RestaurantRepository;
import com.ipsator.foodOrderingSystem.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingServiceImpl implements RatingService{

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    RatingRepository ratingRepository;
    @Autowired
    UserRepo userRepo;

    @Autowired
    RestaurantRepository restaurantRepository;
    @Override
    public Rating submitRating(Long restaurantId, RatingRequest ratingRequest, String user) {
        User userName = userRepo.findByEmail(user)
                .orElseThrow(() -> new RuntimeException("User not found"));


        Rating rating=new Rating();
        rating.setUserId(userName);
        rating.setRatingValue(ratingRequest.getRatingValue());
        rating.setFeedback(ratingRequest.getFeedback());
        
        Restaurant restaurant=restaurantRepository.findById(restaurantId).orElse(null);
        
        if(restaurant !=null)
        {
            rating.setRestaurant(restaurant);
        }
        
        Rating savedRating=ratingRepository.save(rating);
        
        UpdateRestaurantAverageRating(restaurant);
        
        return savedRating;


    }

    private void UpdateRestaurantAverageRating(Restaurant restaurant) {
        
        double averageRating=calculateAverageRating(restaurant.getId());
        restaurant.setRating(averageRating);
        restaurantRepository.save(restaurant);
    }

    private double calculateAverageRating(Long restaurantId) {
        List<Rating>ratings=ratingRepository.findByRestaurantId(restaurantId);
        if(ratings.isEmpty())
        {
            return 0.0;
        }
        int totalRating=0;
        for(Rating rating : ratings){
            totalRating +=rating.getRatingValue();
        }
        return (double) totalRating/ratings.size();
    }
}
