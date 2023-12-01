package com.ipsator.foodOrderingSystem.service;

import com.ipsator.foodOrderingSystem.dto.FoodItemsDto;
import com.ipsator.foodOrderingSystem.entity.FoodItems;
import com.ipsator.foodOrderingSystem.payload.ServiceResponse;

import java.util.List;

public interface FoodItemsService {

    ServiceResponse<FoodItemsDto> addFoodItems(Long restaurantId,FoodItems foodItems);

    FoodItems getFoodItemById(Long id);

    FoodItems updateFoodItem(Long id, FoodItems updatedFoodItem);

}
