package com.ms.spring.starter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.ms.spring.starter.entity.Role;
import com.ms.spring.starter.exception.NotFoundException;
import com.ms.spring.starter.repository.RoleRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repo;

    @Override
    public Role create(Role role) {
        return repo.save(role);
    }

    @Override
    public List<Role> getAll() {
        return repo.findAll();
    }

    @Override
    public Role getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Role not found"));
    }
}