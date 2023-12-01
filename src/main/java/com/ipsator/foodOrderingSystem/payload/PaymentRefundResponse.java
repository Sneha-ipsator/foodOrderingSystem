package com.ipsator.foodOrderingSystem.payload;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentRefundResponse<T> {
    private Boolean success;
    private T refund;
    private String message;
}
