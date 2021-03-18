package io.cloudmobility.tiago.security.jwt;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(name = "JwtRequest", description = "Request for Jwt token")
public class JwtRequestDto implements Serializable {

    @Schema(description = "User's name", example = "patient")
    private String username;

    @Schema(description = "User's password", example = "password")
    private String password;

}