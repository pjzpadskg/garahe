package com.pj.garahe.dto;

import lombok.Builder;

@Builder
public record AuthenticatedUser(
        String token,
        String firstName,
        String lastName,
        String contactNumber
) {
}
