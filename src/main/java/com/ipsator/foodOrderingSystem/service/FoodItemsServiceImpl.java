package com.ipsator.foodOrderingSystem.service;
import com.ipsator.foodOrderingSystem.dto.FoodItemsDto;
import com.ipsator.foodOrderingSystem.entity.FoodItems;
import com.ipsator.foodOrderingSystem.entity.Restaurant;
import com.ipsator.foodOrderingSystem.payload.ServiceResponse;
import com.ipsator.foodOrderingSystem.repository.FoodItemsRepository;
import com.ipsator.foodOrderingSystem.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class FoodItemsServiceImpl implements FoodItemsService{
    @Autowired
    FoodItemsRepository foodItemsRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Override
    public ServiceResponse<FoodItemsDto> addFoodItems(Long restaurantId,FoodItems foodItems) {
        try {
            boolean foodItemExists = foodItemsRepository.existsByNameAndRestaurantId(foodItems.getName(), restaurantId);
            Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new RuntimeException("restaurant does not exist"));


            if (foodItemExists) {
                return new ServiceResponse<>(false, null, "FoodItem Already Exist");
            }
            else {
                foodItems.setRestaurant(restaurant);

                if (foodItems.isLimitedTimeItem()) {
                    LocalDateTime startTime = foodItems.getStartTime();
                    LocalDateTime endTime = foodItems.getEndTime();
                    LocalDateTime currentTime = LocalDateTime.now();
                    foodItems.setEnabled(currentTime.isEqual(startTime) || (currentTime.isAfter(startTime) && currentTime.isBefore(endTime)));
                }
                else {
                    foodItems.setEnabled(true);
                }
                FoodItems savedFoodItems = foodItemsRepository.save(foodItems);

                FoodItemsDto foodItemsDto = new FoodItemsDto();

                foodItemsDto.setId(savedFoodItems.getId());
                foodItemsDto.setFoodType(savedFoodItems.getFoodType());
                foodItemsDto.setName(savedFoodItems.getName());
                foodItemsDto.setPrice(savedFoodItems.getPrice());
                foodItemsDto.setCuisine(savedFoodItems.getCuisine());
                foodItemsDto.setLimitedTimeItem(savedFoodItems.isLimitedTimeItem());
                foodItemsDto.setEnabled(savedFoodItems.isEnabled());
                foodItemsDto.setStartTime(savedFoodItems.getStartTime());
                foodItemsDto.setEndTime(savedFoodItems.getEndTime());
                foodItemsDto.setFilter(savedFoodItems.getFilter());

                return new ServiceResponse<>(true, foodItemsDto, "Food item added successfully");
            }
        }
        catch (Exception ex)
        {
            return new ServiceResponse<>(false, null, ex.getMessage());
        }

    }
    public FoodItems getFoodItemById(Long id) {
        return foodItemsRepository.findById(id).orElse(null);
    }

    public FoodItems updateFoodItem(Long id, FoodItems updatedFoodItem) {
        if (foodItemsRepository.existsById(id)) {
            updatedFoodItem.setId(id);
            return foodItemsRepository.save(updatedFoodItem);
        } else {
            return null;
        }
    }

}


