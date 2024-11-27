package com.backend.aula09.dto;

import com.backend.aula09.model.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa os dados necessários para registrar um usuário")
public record RegisterDTO(
        String login,
        String password,
        UserRole role,
        String name,
        String email,
        String phone) {
}
