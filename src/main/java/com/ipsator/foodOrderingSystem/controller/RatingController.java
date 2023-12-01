package com.ipsator.foodOrderingSystem.controller;

import com.ipsator.foodOrderingSystem.entity.Rating;
import com.ipsator.foodOrderingSystem.payload.ApiResponse;
import com.ipsator.foodOrderingSystem.payload.Error;
import com.ipsator.foodOrderingSystem.payload.RatingRequest;
import com.ipsator.foodOrderingSystem.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/rating")
public class RatingController {
    @Autowired
    RatingService ratingService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/add/{restaurantId}")
    public ResponseEntity<ApiResponse>submitRating(@PathVariable Long restaurantId, @RequestBody RatingRequest ratingRequest, Principal principal)
    {
        try {
            String user = principal.getName();
            Rating rating = ratingService.submitRating(restaurantId, ratingRequest, user);
           if(rating!=null)
           {
               return new ResponseEntity<>(new ApiResponse("success","Thank You",null), HttpStatus.OK);
           }
           else {
               return new ResponseEntity<>(new ApiResponse("success","failed",null),HttpStatus.BAD_REQUEST);
           }
        }
        catch (Exception ex)
        {
            return new ResponseEntity<>(new ApiResponse("success",ex.getMessage(),new Error("Something went wrong")),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
