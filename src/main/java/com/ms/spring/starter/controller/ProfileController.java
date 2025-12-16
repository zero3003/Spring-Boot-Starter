package com.ms.spring.starter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ms.spring.starter.Util.SecurityUtil;
import com.ms.spring.starter.dto.ApiResponse;
import com.ms.spring.starter.dto.ChangePasswordRequest;
import com.ms.spring.starter.dto.ProfileUpdate;
import com.ms.spring.starter.dto.UserRequest;
import com.ms.spring.starter.dto.UserResponse;
import com.ms.spring.starter.service.UserService;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ProfileController {

    private final UserService service;

    @GetMapping()
    public ResponseEntity<?> get() {
        Long userId = SecurityUtil.currentUserId();

        UserResponse data = service.getById(userId);
        return ResponseEntity.ok(
                ApiResponse.success(data, "Data Fetched Successfully"));
    }

    @PutMapping()
    public ResponseEntity<?> update(@Valid @RequestBody ProfileUpdate req) {
        try {
            System.out.println("Profile Update Request: " + req);
            Long userId = SecurityUtil.currentUserId();

            UserRequest userReq = UserRequest.builder()
                    .name(req.getName())
                    .email(req.getEmail())
                    .username(req.getUsername())
                    .build();

            service.checkUniqueUserExcept(userReq, userId);

            UserResponse data = service.updateProfile(req, userId);
            return ResponseEntity.ok(
                    ApiResponse.success(data, "Profile Updated Successfully"));

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Profile Update failed: " + e.getMessage()));
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @Valid @RequestBody ChangePasswordRequest req) {
        try {
            Long userId = SecurityUtil.currentUserId();

            service.changePassword(req, userId);
            return ResponseEntity.ok(
                    ApiResponse.success(null, "Password Changed Successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Change Password failed: " + e.getMessage()));
        }
    }
}
