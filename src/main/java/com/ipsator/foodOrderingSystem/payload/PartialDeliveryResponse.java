package com.ipsator.foodOrderingSystem.payload;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PartialDeliveryResponse {
    private Long orderId;
    private Long shipmentId;



}
