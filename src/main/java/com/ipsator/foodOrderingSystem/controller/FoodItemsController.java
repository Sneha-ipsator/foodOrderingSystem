package com.ipsator.foodOrderingSystem.controller;

import com.ipsator.foodOrderingSystem.dto.FoodItemsDto;
import com.ipsator.foodOrderingSystem.entity.FoodItems;
import com.ipsator.foodOrderingSystem.payload.ApiResponse;
import com.ipsator.foodOrderingSystem.payload.Error;
import com.ipsator.foodOrderingSystem.payload.ServiceResponse;
import com.ipsator.foodOrderingSystem.service.FoodItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/foodItems")
public class FoodItemsController {
    @Autowired
    FoodItemsService foodItemsService;
    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
    @PostMapping("/{restaurantId}")
    public ResponseEntity<ApiResponse> addFoodItems(@PathVariable Long restaurantId,@RequestBody FoodItems foodItems)
    {
        try {
            ServiceResponse<FoodItemsDto> response = foodItemsService.addFoodItems(restaurantId,foodItems);
            if(response.getSuccess()) {
                return new ResponseEntity<>(new ApiResponse("success", response.getData(), null), HttpStatus.CREATED);
            }

            return new ResponseEntity<>(new ApiResponse("Error", null, new Error("Restaurant Id Not Found")), HttpStatus.NOT_FOUND);

        }
        catch(Exception ex)
        {
            return new ResponseEntity<>(new ApiResponse("Error",null,new Error(ex.getMessage())),HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<FoodItems> getFoodItemById(@PathVariable Long id) {
        try {
            FoodItems foodItem = foodItemsService.getFoodItemById(id);
            if (foodItem != null) {
                return new ResponseEntity<>(foodItem, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        catch(Exception ex)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_RESTAURANT')")
    @PutMapping("/{id}")
    public ResponseEntity<FoodItems> updateFoodItem(@PathVariable Long id, @RequestBody FoodItems updatedFoodItem) {
        try {
            FoodItems foodItem = foodItemsService.updateFoodItem(id, updatedFoodItem);
            if (foodItem != null) {
                return new ResponseEntity<>(foodItem, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        catch(Exception ex)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
