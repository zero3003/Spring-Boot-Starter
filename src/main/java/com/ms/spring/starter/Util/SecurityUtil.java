package com.ms.spring.starter.Util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.ms.spring.starter.service.CustomUserDetails;

public final class SecurityUtil {

    private SecurityUtil() {
        // prevent instantiation
    }

    public static Long currentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return 0L; // system / anonymous
        }

        Object principal = auth.getPrincipal();

        if (principal instanceof CustomUserDetails cud) {
            return cud.getId();
        }

        return 0L;
    }
}
