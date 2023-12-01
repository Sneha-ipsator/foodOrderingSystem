package com.ipsator.foodOrderingSystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ipsator.foodOrderingSystem.entity.FoodItems;
import com.ipsator.foodOrderingSystem.entity.Order;
import jakarta.persistence.*;

public class OrderItemDto {
    private Long orderItemId;
    private FoodItemsDto foodItem;

    private double totalFooditemPrice;

    @JsonIgnore
    private OrderDto order;

    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public FoodItemsDto getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(FoodItemsDto foodItem) {
        this.foodItem = foodItem;
    }

    public double getTotalFooditemPrice() {
        return totalFooditemPrice;
    }

    public void setTotalFooditemPrice(double totalFooditemPrice) {
        this.totalFooditemPrice = totalFooditemPrice;
    }

    public OrderDto getOrder() {
        return order;
    }

    public void setOrder(OrderDto order) {
        this.order = order;
    }
}
