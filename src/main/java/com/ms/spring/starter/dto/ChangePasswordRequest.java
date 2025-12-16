package com.ms.spring.starter.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ms.spring.starter.validation.PasswordMatches;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@PasswordMatches
public class ChangePasswordRequest {

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 64, message = "Password must be 8–64 characters")
    private String password;

    @NotBlank(message = "Confirm password is required")
    @Size(min = 8, max = 64, message = "Confirm password must be 8–64 characters")
    @JsonProperty("confirm_password")
    private String confirmPassword;
}