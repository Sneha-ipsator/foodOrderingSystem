package com.ipsator.foodOrderingSystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CartItemDto {
    private Long cartItemId;
    private Integer quantity;
    private double totalPrice;

    @JsonIgnore
    private CartDto cart;

    private FoodItemsDto foodItems;

    public Long getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public CartDto getCart() {
        return cart;
    }

    public void setCart(CartDto cart) {
        this.cart = cart;
    }

    public FoodItemsDto getFoodItems() {
        return foodItems;
    }

    public void setFoodItems(FoodItemsDto foodItems) {
        this.foodItems = foodItems;
    }
}
