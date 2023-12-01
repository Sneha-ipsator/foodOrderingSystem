package com.ipsator.foodOrderingSystem.service;

import com.ipsator.foodOrderingSystem.dto.RestaurantDto;
import com.ipsator.foodOrderingSystem.entity.FoodItems;
import com.ipsator.foodOrderingSystem.entity.Restaurant;
import com.ipsator.foodOrderingSystem.payload.ServiceResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RestaurantService {
    ServiceResponse<RestaurantDto> createRestaurant(Restaurant restaurant);

    Restaurant getRestaurantById(Long id);


    List<Restaurant> getAllRestaurants();

    Restaurant updateRestaurant(Long id, Restaurant restaurant);

    Boolean deleteRestaurant(Long id);

    List<FoodItems> getFoodItemsByRestaurantId(Long restaurantId);
}
