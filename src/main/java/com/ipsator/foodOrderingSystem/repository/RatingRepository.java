package com.ipsator.foodOrderingSystem.repository;

import com.ipsator.foodOrderingSystem.entity.Rating;
import com.ipsator.foodOrderingSystem.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating,Long> {
    List<Rating> findByRestaurantId(Long restaurantId);
}
