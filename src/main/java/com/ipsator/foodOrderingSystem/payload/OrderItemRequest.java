package com.ipsator.foodOrderingSystem.payload;

public class OrderItemRequest {
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

//    public double getTotalFoodItemPrice() {
//        return totalFoodItemPrice;
//    }
//
//    public void setTotalFoodItemPrice(double totalFoodItemPrice) {
//        this.totalFoodItemPrice = totalFoodItemPrice;
//    }
}
