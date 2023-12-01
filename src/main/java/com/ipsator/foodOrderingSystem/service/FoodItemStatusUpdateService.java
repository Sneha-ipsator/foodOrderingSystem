package com.ipsator.foodOrderingSystem.service;

import com.ipsator.foodOrderingSystem.entity.FoodItems;
import com.ipsator.foodOrderingSystem.repository.FoodItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FoodItemStatusUpdateService {

    @Autowired
    private FoodItemsRepository foodItemsRepository;

    @Scheduled(fixedRate = 60000)
    public void updateFoodItemStatus() {
        List<FoodItems> expiredItems = foodItemsRepository.findByEndTimeBeforeAndEnabledTrue(LocalDateTime.now());
        for (FoodItems foodItem : expiredItems) {
            foodItem.setEnabled(false);
            foodItemsRepository.save(foodItem);
        }
    }
}
