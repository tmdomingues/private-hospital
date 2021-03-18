package io.cloudmobility.tiago.security.jwt;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "JwtResponse")
public class JwtResponseDto implements Serializable {

    @Schema(description = "User's name", example = "patient")
    private final String jwtToken;
}
