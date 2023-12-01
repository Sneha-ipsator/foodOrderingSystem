package com.ipsator.foodOrderingSystem.service;

import com.ipsator.foodOrderingSystem.dto.RestaurantDto;
import com.ipsator.foodOrderingSystem.dto.UserDto;
import com.ipsator.foodOrderingSystem.entity.Address;
import com.ipsator.foodOrderingSystem.entity.FoodItems;
import com.ipsator.foodOrderingSystem.entity.Restaurant;
import com.ipsator.foodOrderingSystem.entity.User;
import com.ipsator.foodOrderingSystem.payload.ApiResponse;
import com.ipsator.foodOrderingSystem.payload.ServiceResponse;
import com.ipsator.foodOrderingSystem.repository.AddressRepository;
import com.ipsator.foodOrderingSystem.repository.FoodItemsRepository;
import com.ipsator.foodOrderingSystem.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class RestaurantServiceImpl implements RestaurantService{
    @Autowired
     RestaurantRepository restaurantRepository;

    @Autowired
    FoodItemsRepository foodItemsRepository;

    @Autowired
    private AddressRepository addressRepository;

    public ServiceResponse<RestaurantDto> createRestaurant(Restaurant restaurant) {
        try {
            Optional<Restaurant> existingRestaurant = restaurantRepository.findByName(restaurant.getName());

            if (existingRestaurant.isEmpty()) {
                Address restaurantAddress = restaurant.getAddress();
                if (restaurantAddress != null && restaurantAddress.getId() == null) {
                    Address savedAddress = addressRepository.save(restaurantAddress);
                    restaurant.setAddress(savedAddress);
                }
                Restaurant savedRestaurant = restaurantRepository.save(restaurant);

                RestaurantDto restaurantDto = new RestaurantDto();
                restaurantDto.setId(savedRestaurant.getId());
                restaurantDto.setName(savedRestaurant.getName());
                restaurantDto.setAddress(savedRestaurant.getAddress());
                restaurantDto.setDescription(savedRestaurant.getDescription());

                return new ServiceResponse<>(true, restaurantDto, "Restaurant added successfully");
            } else {
                return new ServiceResponse<>(false, null, "Failed to add restaurant: Restaurant with the same name already exists");
            }
        } catch (Exception e) {
            return new ServiceResponse<>(false, null, "Failed to add restaurant: " + e.getMessage());
        }
    }





    @Override
    public Restaurant getRestaurantById(Long id) {
        return restaurantRepository.findById(id).orElse(null);
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    @Override
    public Restaurant updateRestaurant(Long id, Restaurant restaurant) {
        if (restaurantRepository.existsById(id)) {
            restaurant.setId(id);
            return restaurantRepository.save(restaurant);
        } else {
            return null;
        }
    }

    @Override
    public Boolean deleteRestaurant(Long id) {
        if(restaurantRepository.existsById(id))
        {
           restaurantRepository.deleteById(id);
           return  true;
        }
        else {
            return false;
        }

    }

    @Override
    public List<FoodItems> getFoodItemsByRestaurantId(Long restaurantId) {
        return foodItemsRepository.findEnabledFoodItemsByRestaurantId(restaurantId);
    }


}
