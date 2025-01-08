package com.backend.security;

import com.backend.model.RolName;
import com.backend.model.User;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class JwtTokenUtils {

   private

    JwtTokenUtils() {
    }

    public static Optional<User> getCurrentUser() {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof User user) {
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }
    public static boolean isAdminCurrentUser() {

        if (getCurrentUser().isEmpty()) {
            return false;
        }

        User user = getCurrentUser().get();

        return user.getRolName().equals(RolName.ADMIN);

    }
}
