package com.ms.spring.starter.service;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ms.spring.starter.Util.SecurityUtil;
import com.ms.spring.starter.dto.ChangePasswordRequest;
import com.ms.spring.starter.dto.ProfileUpdate;
import com.ms.spring.starter.dto.UserRequest;
import com.ms.spring.starter.dto.UserResponse;
import com.ms.spring.starter.entity.Role;
import com.ms.spring.starter.entity.User;
import com.ms.spring.starter.exception.NotFoundException;
import com.ms.spring.starter.repository.RoleRepository;
import com.ms.spring.starter.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse create(UserRequest req) {

        Set<Role> roles = new HashSet<>(roleRepo.findAllById(req.getRoleIds()));

        if (roles.isEmpty()) {
            throw new NotFoundException("Roles not found");
        }

        User user = User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .username(req.getUsername())
                .password(passwordEncoder.encode(req.getPassword())) // hash password
                .roles(roles)
                .build();

        userRepo.save(user);

        return toResponse(user);
    }

    @Override
    public Page<UserResponse> getAll(Pageable pageable) {
        return userRepo.findAll(pageable)
                .map(this::toResponse);
    }

    @Override
    public UserResponse getById(Long id) {
        return userRepo.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public UserResponse update(Long id, UserRequest req) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Set<Role> roles = new HashSet<>(roleRepo.findAllById(req.getRoleIds()));

        if (roles.isEmpty()) {
            throw new NotFoundException("Roles not found");
        }

        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setUsername(req.getUsername());
        user.setRoles(roles);

        userRepo.save(user);

        return toResponse(user);
    }

    @Override
    public void delete(Long id) {

        User user = userRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Long actorId = SecurityUtil.currentUserId();

        user.setDeletedAt(LocalDateTime.now());
        user.setDeletedBy(actorId);
        userRepo.save(user);
    }

    @Override
    public void checkUniqueUser(UserRequest req) {
        if (userRepo.findByUsername(req.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userRepo.findByEmail(req.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
    }

    @Override
    public void checkUniqueUserExcept(UserRequest req, long excludeId) {

        if (userRepo.existsByUsernameAndIdNot(req.getUsername(), excludeId)) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userRepo.existsByEmailAndIdNot(req.getEmail(), excludeId)) {
            throw new IllegalArgumentException("Email already exists");
        }
    }

    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .username(user.getUsername())
                .roles(
                        user.getRoles())
                .profilePicturePath(user.getProfilePicturePath())
                .build();
    }

    @Override
    public void changePassword(ChangePasswordRequest req, long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        user.setPassword(passwordEncoder.encode(req.getPassword())); // hash password

        userRepo.save(user);
    }

    @Override
    public UserResponse updateProfile(ProfileUpdate req, long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setUsername(req.getUsername());

        userRepo.save(user);

        return toResponse(user);
    }

    @Override
    public void updateProfilePicturePath(long userId, String path) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        user.setProfilePicturePath(path);

        userRepo.save(user);
    }

}
