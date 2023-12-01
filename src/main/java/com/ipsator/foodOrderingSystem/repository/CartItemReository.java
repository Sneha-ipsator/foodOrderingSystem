package com.ipsator.foodOrderingSystem.repository;

import com.ipsator.foodOrderingSystem.entity.CartItem;
import com.ipsator.foodOrderingSystem.entity.FoodItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemReository extends JpaRepository<CartItem, Long> {
    
}
