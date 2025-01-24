package com.sneakysquid.authenticator.service;

import com.sneakysquid.authenticator.configuration.JWTService;
import com.sneakysquid.authenticator.domain.User;
import com.sneakysquid.authenticator.repo.UserRepository;
import com.sneakysquid.authenticator.domain.dto.request.AuthenticationRequest;
import com.sneakysquid.authenticator.domain.dto.request.RegisterRequest;
import com.sneakysquid.authenticator.domain.dto.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public AuthenticationResponse register(RegisterRequest request) {
        User user = new User();
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        // TODO: Fix roles and groups make sense
        //  user.setRole(RoleType.ROLE_OWNER);
        user = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        var jwtToken = jwtService.generateToken(userDetails);
        return new AuthenticationResponse(jwtToken);
    }
}
