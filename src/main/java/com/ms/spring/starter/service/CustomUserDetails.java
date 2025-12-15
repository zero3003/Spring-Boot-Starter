package com.ms.spring.starter.service;

import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.ms.spring.starter.entity.User;

public class CustomUserDetails extends org.springframework.security.core.userdetails.User {

    private final Long id;

    public CustomUserDetails(User user) {
        super(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getName()))
                        .collect(Collectors.toList()));
        this.id = user.getId();
    }

    public Long getId() {
        return id;
    }
}
