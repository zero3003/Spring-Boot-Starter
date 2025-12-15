package com.ms.spring.starter.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.ms.spring.starter.entity.Role;
import com.ms.spring.starter.service.RoleService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class RoleController {

    private final RoleService service;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Role create(@RequestBody Role r) {
        return service.create(r);
    }

    @GetMapping
    public List<Role> getAll() {
        return service.getAll();
    }
}