package com.pj.garahe.service;

import com.pj.garahe.dto.AuthenticatedUser;
import com.pj.garahe.dto.UserCreationRequest;
import com.pj.garahe.dto.UserLoginRequest;
import com.pj.garahe.entity.User;
import com.pj.garahe.exception.ResourceConflictException;
import com.pj.garahe.exception.ResourceNotFoundException;
import com.pj.garahe.repo.UserRepository;
import com.pj.garahe.security.JwtExtraClaims;
import com.pj.garahe.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationProvider authenticationProvider;
    private final JwtService jwtService;

    public AuthenticatedUser authenticate(UserLoginRequest request) {
        authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(
                request.email(), request.password()
        ));

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
        String token = jwtService.generateToken(new JwtExtraClaims(user.getId()), user);
        return AuthenticatedUser.builder()
                .token(token)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .contactNumber(user.getContactNumber())
                .build();
    }

    public AuthenticatedUser create(UserCreationRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent())
            throw new ResourceConflictException("User already exists.");

        User user = new User();
        user.setRole(User.Role.USER);
        user.setEmail(request.email());
        String encryptedPassword = passwordEncoder.encode(request.password());
        user.setPassword(encryptedPassword);
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setContactNumber(request.contactNumber());
        userRepository.save(user);
        return authenticate(new UserLoginRequest(request.email(), request.password()));
    }

}
