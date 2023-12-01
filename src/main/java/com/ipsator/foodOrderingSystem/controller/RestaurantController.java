package com.ipsator.foodOrderingSystem.controller;

import com.ipsator.foodOrderingSystem.dto.RestaurantDto;
import com.ipsator.foodOrderingSystem.entity.FoodItems;
import com.ipsator.foodOrderingSystem.entity.Restaurant;
import com.ipsator.foodOrderingSystem.payload.ApiResponse;
import com.ipsator.foodOrderingSystem.payload.Error;
import com.ipsator.foodOrderingSystem.payload.ServiceResponse;
import com.ipsator.foodOrderingSystem.service.FoodItemsService;
import com.ipsator.foodOrderingSystem.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {
    @Autowired
    RestaurantService restaurantService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse>createRestaurant(@RequestBody Restaurant restaurant){
        try{
            ServiceResponse<RestaurantDto>response=restaurantService.createRestaurant(restaurant);
            if (response.getSuccess()) {
                return new ResponseEntity<>(new ApiResponse("success", response.getData(), null),
                        HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(new ApiResponse("error", null, new Error(response.getMessage())),
                        HttpStatus.CONFLICT); // Use HTTP status code 409 for conflict
            }
        }
        catch(Exception ex)
        {
            return new ResponseEntity<>(new ApiResponse("error",null,new Error(ex.getMessage())), HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Long id) {
        try {
            Restaurant restaurant = restaurantService.getRestaurantById(id);
            if (restaurant != null) {
                return new ResponseEntity<>(restaurant, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        catch(Exception ex)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @GetMapping("/")
    public ResponseEntity<List<Restaurant>> getAllRestaurant() {
        try {
            List<Restaurant> restaurants = restaurantService.getAllRestaurants();

            if (!restaurants.isEmpty()) {
                return new ResponseEntity<>(restaurants, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        catch(Exception ex)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_RESTAURANT','ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable Long id,@RequestBody Restaurant restaurant)
    {
     try{
         Restaurant restaurants=restaurantService.updateRestaurant(id,restaurant);
         if(restaurants !=null)
         {
             return new ResponseEntity<>(restaurant, HttpStatus.OK);
         }
         else {
             return new ResponseEntity<>(restaurant, HttpStatus.NOT_FOUND);
         }
     }
     catch(Exception ex)
     {
         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
     }
    }

    @GetMapping("/{restaurantId}/foodItems")
    public ResponseEntity<List<FoodItems>> getFoodItemsByRestaurantId(@PathVariable Long restaurantId) {
        try {
            List<FoodItems> foodItems = restaurantService.getFoodItemsByRestaurantId(restaurantId);

            if (!foodItems.isEmpty()) {
                return new ResponseEntity<>(foodItems, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        catch(Exception ex)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse>deleteRestaurant(@PathVariable Long id)
    {
        try{
           Boolean deleted=restaurantService.deleteRestaurant(id);
           if(deleted) {
               return new ResponseEntity<>(new ApiResponse("success","Restaurant Deleted Successfully",null),HttpStatus.OK);
           }
           else {
               return new ResponseEntity<>(new ApiResponse("Error",null,new Error("ID not found")),HttpStatus.NOT_FOUND);
           }

        }
        catch(Exception ex)
        {
            return new ResponseEntity<>(new ApiResponse("Error",null,new Error("Something Went wrong")),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
