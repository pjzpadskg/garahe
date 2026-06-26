package com.pj.garahe.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record BaseUserDetail(
        UUID id,
        String email,
        String firstName,
        String lastName,
        String contactNumber
) {
}
