package com.pj.garahe.controller;

import com.pj.garahe.dto.AuthenticatedUser;
import com.pj.garahe.dto.UserCreationRequest;
import com.pj.garahe.dto.UserLoginRequest;
import com.pj.garahe.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/sessions")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticatedUser login(@Valid @RequestBody UserLoginRequest request) {
        return userService.authenticate(request);
    }

    @PostMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticatedUser create(@Valid @RequestBody UserCreationRequest request) {
        return userService.create(request);
    }
}
