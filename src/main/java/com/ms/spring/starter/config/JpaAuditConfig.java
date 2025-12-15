package com.ms.spring.starter.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.ms.spring.starter.service.CustomUserDetails;

@EnableJpaAuditing
@Configuration
public class JpaAuditConfig {

    @Bean
    public AuditorAware<Long> auditorAware() {
        return () -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth == null || !auth.isAuthenticated()) {
                return Optional.of(0L); // system / migration
            }

            Object principal = auth.getPrincipal();

            if (principal instanceof CustomUserDetails cud) {
                return Optional.of(cud.getId());
            }

            // fallback (should not happen if configured correctly)
            return Optional.of(0L);
        };
    }
}
