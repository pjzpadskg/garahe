package com.pj.garahe.security;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

public record SecurityDetail(
        WebAuthenticationDetails webDetails,
        JwtExtraClaims extraClaims
) {
}
