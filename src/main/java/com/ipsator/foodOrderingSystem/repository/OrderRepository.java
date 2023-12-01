package com.ipsator.foodOrderingSystem.repository;

import com.ipsator.foodOrderingSystem.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByUserEmail(String username);

    List<Order>findByPastOrder(Order order);
}
