package com.ms.spring.starter.dto;

import lombok.Data;
import java.util.Set;

@Data
public class UserRequest {
    private String name;
    private String email;
    private String username;
    private String password;   // plain, service will hash it
    private Set<Long> roleIds; // multiple roles
}