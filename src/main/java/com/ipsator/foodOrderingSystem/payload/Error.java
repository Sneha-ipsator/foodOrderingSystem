package com.ipsator.foodOrderingSystem.payload;

import lombok.*;

/**
 * This class represents an error message.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Error {
    private String message;
}
