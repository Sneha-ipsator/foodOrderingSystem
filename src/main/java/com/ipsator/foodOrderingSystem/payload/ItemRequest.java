package com.ipsator.foodOrderingSystem.payload;

public class ItemRequest {
    private Long foodItemId;
    private int quantity;

    public Long getFoodItemId() {
        return foodItemId;
    }

    public void setFoodItemId(Long foodItemId) {
        this.foodItemId = foodItemId;
    }

    public int getQuantity() {
        return quantity;
    }


    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public ItemRequest() {
    }

    public ItemRequest(Long foodItemId, int quantity) {
        this.foodItemId = foodItemId;
        this.quantity = quantity;
    }
}
