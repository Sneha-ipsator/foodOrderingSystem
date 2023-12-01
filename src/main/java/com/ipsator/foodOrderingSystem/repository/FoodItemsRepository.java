package com.ipsator.foodOrderingSystem.repository;

import com.ipsator.foodOrderingSystem.entity.FoodItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FoodItemsRepository extends JpaRepository<FoodItems,Long> {
    boolean existsByNameAndRestaurantId(String name, Long restaurantId);

    List<FoodItems> findByRestaurantId(Long restaurantId);

    List<FoodItems> findByEndTimeBeforeAndEnabledTrue(LocalDateTime now);
    @Query("SELECT f FROM FoodItems f WHERE f.restaurant.id = :restaurantId AND f.enabled = true")
    List<FoodItems> findEnabledFoodItemsByRestaurantId(@Param("restaurantId") Long restaurantId);

}
