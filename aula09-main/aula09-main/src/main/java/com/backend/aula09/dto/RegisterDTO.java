package com.backend.aula09.dto;

import com.backend.aula09.model.UserRole;

public record RegisterDTO(
        String login,
        String password,
        UserRole role,
        String name,
        String email,
        String phone) {
}
