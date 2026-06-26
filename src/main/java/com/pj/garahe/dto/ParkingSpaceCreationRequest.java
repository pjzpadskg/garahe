package com.pj.garahe.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ParkingSpaceCreationRequest(
        String address,
        @NotNull @PositiveOrZero @Digits(integer = 8, fraction = 2) BigDecimal price
) {
}
