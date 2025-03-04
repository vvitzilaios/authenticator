package com.sneakysquid.authenticator.service;

import com.sneakysquid.authenticator.domain.Group;
import com.sneakysquid.authenticator.domain.User;
import com.sneakysquid.authenticator.domain.dto.UserDto;
import com.sneakysquid.authenticator.domain.dto.response.AuthenticationResponse;
import com.sneakysquid.authenticator.domain.enums.GroupType;
import com.sneakysquid.authenticator.repository.GroupRepository;
import com.sneakysquid.authenticator.repository.UserRepository;
import com.sneakysquid.authenticator.domain.dto.request.AuthenticationRequest;
import com.sneakysquid.authenticator.domain.dto.request.RegisterRequest;
import com.sneakysquid.authenticator.transform.UserMapper;
import com.sneakysquid.authenticator.util.JwtUtil;
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
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final GroupRepository groupRepository;
    private final UserMapper userMapper;

    public UserDto register(RegisterRequest request) {
        Optional<Group> group = groupRepository.findByName(GroupType.USER.name());
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setGroups(Set.of(group.orElse(new Group(GroupType.USER.name()))));
        return userMapper.toDto(userRepository.save(user));
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = JwtUtil.generateToken(userDetails);
        return AuthenticationResponse.builder()
                .message("You have successfully logged in.")
                .token(token)
                .build();
    }
}
