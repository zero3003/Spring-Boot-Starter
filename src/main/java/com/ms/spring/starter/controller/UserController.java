package com.ms.spring.starter.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.ms.spring.starter.dto.ApiResponse;
import com.ms.spring.starter.dto.ChangePasswordRequest;
import com.ms.spring.starter.dto.UserRequest;
import com.ms.spring.starter.dto.UserResponse;
import com.ms.spring.starter.service.UserService;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserService service;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody UserRequest req) {
        try {
            // get user first by username and emai to avoid duplicates
            service.checkUniqueUser(req);

            UserResponse data = service.create(req);
            return ResponseEntity.ok(
                    ApiResponse.success(data, "User Created Successfully"));

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("User Creation failed: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAll(@PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable) {
        Page<UserResponse> page = service.getAll(pageable);

        return ResponseEntity.ok(
                ApiResponse.success(
                        page.getContent(),
                        "Data fetched successfully",
                        Map.of(
                                "page", page.getNumber(),
                                "size", page.getSize(),
                                "totalElements", page.getTotalElements(),
                                "totalPages", page.getTotalPages())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @Parameter(description = "User ID", required = true) @PathVariable("id") Long id) {

        try {
            UserResponse data = service.getById(id);
            return ResponseEntity.ok(
                    ApiResponse.success(data, "Data Fetched Successfully"));

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Get Users failed: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Parameter(description = "User ID", required = true) @PathVariable("id") Long id,
            @Valid @RequestBody UserRequest req) {
        try {
            service.checkUniqueUserExcept(req, id);

            UserResponse data = service.update(id, req);
            return ResponseEntity.ok(
                    ApiResponse.success(data, "User Updated Successfully"));

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("User Update failed: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@Parameter(description = "User ID", required = true) @PathVariable("id") Long id) {
        try {
            service.delete(id);
            return ResponseEntity.ok(
                    ApiResponse.success(null, "User Deleted Successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("User Delete failed: " + e.getMessage()));
        }
    }

    @PutMapping("/change-password/{id}")
    public ResponseEntity<?> changePassword(
            @Parameter(description = "User ID", required = true) @PathVariable("id") Long id,
            @Valid @RequestBody ChangePasswordRequest req) {
        try {
            service.changePassword(req, id);
            return ResponseEntity.ok(
                    ApiResponse.success(null, "Password Changed Successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Change Password failed: " + e.getMessage()));
        }
    }

}
