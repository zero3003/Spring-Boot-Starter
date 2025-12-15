package com.ms.spring.starter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ms.spring.starter.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
