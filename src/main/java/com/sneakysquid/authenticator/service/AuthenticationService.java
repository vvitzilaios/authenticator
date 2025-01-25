package com.sneakysquid.authenticator.service;

import com.sneakysquid.authenticator.configuration.JWTService;
import com.sneakysquid.authenticator.domain.Group;
import com.sneakysquid.authenticator.domain.User;
import com.sneakysquid.authenticator.domain.enums.GroupType;
import com.sneakysquid.authenticator.repository.GroupRepository;
import com.sneakysquid.authenticator.repository.UserRepository;
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

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final GroupRepository groupRepository;

    public AuthenticationResponse register(RegisterRequest request) {
        Optional<Group> group = groupRepository.findByName(GroupType.USER.name());
        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setGroups(Set.of(group.orElse(new Group(GroupType.USER.name()))));
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
