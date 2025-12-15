package com.ms.spring.starter.service;

import java.util.List;

import com.ms.spring.starter.entity.Role;

public interface RoleService {
    Role create(Role role);
    List<Role> getAll();
    Role getById(Long id);
}
