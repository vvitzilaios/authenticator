package com.sneakysquid.authenticator.service;

import com.sneakysquid.authenticator.domain.User;
import com.sneakysquid.authenticator.domain.dto.UserDto;
import com.sneakysquid.authenticator.repository.UserRepository;
import com.sneakysquid.authenticator.transform.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDto> search(String username) {
        return userMapper.toDtoList(
                userRepository.findByUsernameContainingIgnoreCaseOrderByUsernameAsc(username));
    }

    public UserDto save(UserDto dto) {
        User user = userMapper.toEntity(dto);
        return userMapper.toDto(userRepository.save(user));
    }

    public void delete(UserDto dto) {
        User user = userMapper.toEntity(dto);
        userRepository.delete(user);
    }
}
