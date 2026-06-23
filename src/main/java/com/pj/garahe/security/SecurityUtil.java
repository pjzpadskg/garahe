package com.pj.garahe.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class SecurityUtil {

    private final ObjectMapper objectMapper;

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public JwtExtraClaims getCurrentUserClaims() {
        Authentication authentication = getAuthentication();
        if (authentication == null || !authentication.isAuthenticated())
            throw new IllegalStateException("No authenticated user found in security context");

        if (authentication.getDetails() instanceof SecurityDetail details) return details.extraClaims();
        throw new IllegalStateException("No extra claims found in security context");
    }

    public String getCurrentEmail() {
        Authentication authentication = getAuthentication();
        if (authentication == null || !authentication.isAuthenticated())
            throw new IllegalStateException("No authenticated user found in security context");
        return authentication.getName();
    }
}
