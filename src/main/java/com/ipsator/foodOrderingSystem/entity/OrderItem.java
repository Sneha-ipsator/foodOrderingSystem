package com.ipsator.foodOrderingSystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private FoodItems foodItem;

    private double totalFooditemPrice;

    //Particular food Item Price if any changes occuur in fooDItem Price we can fetch previce order Item price from there

    private int fooditemQuantity;

    private int receivedQuantity;

    @ManyToOne
    private Order order;

    public int getReceivedQuantity() {
        return receivedQuantity;
    }

    public void setReceivedQuantity(int receivedQuantity) {
        this.receivedQuantity = receivedQuantity;
    }
    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public FoodItems getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(FoodItems foodItem) {
        this.foodItem = foodItem;
    }

    public double getTotalFooditemPrice() {
        return totalFooditemPrice;
    }

    public void setTotalFooditemPrice(double totalFooditemPrice) {
        this.totalFooditemPrice = totalFooditemPrice;
    }

    public int getFooditemQuantity() {
        return fooditemQuantity;
    }

    public void setFooditemQuantity(int fooditemQuantity) {
        this.fooditemQuantity = fooditemQuantity;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
