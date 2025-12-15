package com.ms.spring.starter.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

import com.ms.spring.starter.entity.Role;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String username;
    private Set<Role> roles;
}