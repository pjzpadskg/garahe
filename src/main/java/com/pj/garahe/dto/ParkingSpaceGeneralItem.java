package com.pj.garahe.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record ParkingSpaceGeneralItem(
        UUID id,
        String address,
        BigDecimal price,
        BaseUserDetail owner
) {
}
