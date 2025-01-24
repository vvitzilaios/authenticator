package com.sneakysquid.authenticator.domain.dto.response;

import lombok.*;

@Setter
@Getter
@Data
@Builder
public class AuthenticationResponse {

    private String token;

    public AuthenticationResponse(String token) {
        this.token = token;
    }

}
