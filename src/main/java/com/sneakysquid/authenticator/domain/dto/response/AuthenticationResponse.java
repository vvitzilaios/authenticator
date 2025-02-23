package com.sneakysquid.authenticator.domain.dto.response;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
public class AuthenticationResponse extends Response {
    private String token;
}
