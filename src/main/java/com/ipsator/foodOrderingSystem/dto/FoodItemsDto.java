package com.ipsator.foodOrderingSystem.dto;

import com.ipsator.foodOrderingSystem.enums.Cuisine;
import com.ipsator.foodOrderingSystem.enums.FoodFilter;
import com.ipsator.foodOrderingSystem.enums.FoodType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDateTime;

public class FoodItemsDto {

        private Long id;
        private String name;
        private double price;


    private FoodType foodType;
    private Cuisine cuisine;

    private boolean limitedTimeItem;
    private boolean enabled;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private FoodFilter filter;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public FoodType getFoodType() {
        return foodType;
    }

    public void setFoodType(FoodType foodType) {
        this.foodType = foodType;
    }

    public Cuisine getCuisine() {
        return cuisine;
    }

    public void setCuisine(Cuisine cuisine) {
        this.cuisine = cuisine;
    }

    public boolean isLimitedTimeItem() {
        return limitedTimeItem;
    }

    public void setLimitedTimeItem(boolean limitedTimeItem) {
        this.limitedTimeItem = limitedTimeItem;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public FoodFilter getFilter() {
        return filter;
    }

    public void setFilter(FoodFilter filter) {
        this.filter = filter;
    }




}
