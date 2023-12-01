package com.ipsator.foodOrderingSystem.repository;

import com.ipsator.foodOrderingSystem.entity.FoodItems;
import com.ipsator.foodOrderingSystem.entity.Order;
import com.ipsator.foodOrderingSystem.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
    List<OrderItem> findByOrderAndFoodItem(Order order, FoodItems foodItem);
}
