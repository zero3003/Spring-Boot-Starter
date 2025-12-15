package com.ms.spring.starter.controller;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.ms.spring.starter.config.JwtUtil;
import com.ms.spring.starter.dto.ApiResponse;
import com.ms.spring.starter.dto.AuthRequest;
import com.ms.spring.starter.dto.RegisterRequest;
import com.ms.spring.starter.entity.Role;
import com.ms.spring.starter.entity.User;
import com.ms.spring.starter.repository.RoleRepository;
import com.ms.spring.starter.repository.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder encoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        try {
            // Validate requested roles exist
            List<Role> foundRoles = roleRepo.findAllById(req.getRoleIds());

            if (foundRoles.size() != req.getRoleIds().size()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("One or more role IDs are invalid"));
            }

            Optional<User> findUser = userRepo.findByUsername(req.getUsername());

            if (findUser.isPresent()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Username already Registered!"));
            }

            findUser = userRepo.findByEmail(req.getEmail());

            if (findUser.isPresent()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Email already Registered!"));
            }

            User user = new User();
            user.setUsername(req.getUsername());
            user.setPassword(encoder.encode(req.getPassword()));
            user.setName(req.getName());
            user.setEmail(req.getEmail());
            user.setRoles(new HashSet<>(foundRoles)); // Convert List → Set

            User saved = userRepo.save(user);

            return ResponseEntity.ok(
                    ApiResponse.success(saved, "User registered successfully"));

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Registration failed: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {

        try {
            System.out.println("LOGIN: " + req.getUsername() + " / " + req.getPassword());
            authManager.authenticate(new UsernamePasswordAuthenticationToken(
                    req.getUsername(), req.getPassword()));

            var user = userRepo.findByUsername(req.getUsername()).orElseThrow();
            String token = jwtUtil.generateToken(req.getUsername());

            // Create the data map
            Map<String, Object> data = new HashMap<>();
            data.put("user", user);
            data.put("token", token);

            return ResponseEntity.ok(
                    ApiResponse.success(data, "Login successful"));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(
                    ApiResponse.error("Invalid username or password"));
        }
    }
}
