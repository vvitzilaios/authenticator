package com.sneakysquid.authenticator.controller;

import com.sneakysquid.authenticator.domain.dto.UserDto;
import com.sneakysquid.authenticator.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/search")
    public List<UserDto> search(@RequestParam String username) {
        return userService.search(username);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto dto = userService.getUserById(id);
        return new ResponseEntity<>(dto, HttpStatusCode.valueOf(HttpServletResponse.SC_OK));
    }

    @PostMapping("/save")
    public ResponseEntity<UserDto> save(@RequestBody UserDto dto) {
        UserDto savedUser = userService.save(dto);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> delete(@RequestBody UserDto dto) {
        userService.delete(dto);
        return ResponseEntity.ok().build();
    }

}
