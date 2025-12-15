package com.ms.spring.starter.service;


import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import com.ms.spring.starter.dto.UserRequest;
import com.ms.spring.starter.dto.UserResponse;

public interface UserService {
    UserResponse create(UserRequest req);
    Page<UserResponse> getAll(Pageable pageable);
    
    UserResponse getById(Long id);
    UserResponse update(Long id, UserRequest req);
    void delete(Long id);
    void checkUniqueUser(UserRequest req);
    void checkUniqueUserExcept(UserRequest req, long excludeId);
}
