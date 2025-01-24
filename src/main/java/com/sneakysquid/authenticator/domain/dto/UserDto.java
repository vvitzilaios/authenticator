package com.sneakysquid.authenticator.domain.dto;

import com.sneakysquid.authenticator.domain.enums.RoleType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private String email;
    private RoleType roleType;
}
