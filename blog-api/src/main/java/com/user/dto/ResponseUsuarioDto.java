package com.user.dto;

import com.user.domain.Role;

public record ResponseUsuarioDto(
        Long id,
        String username,
        String email,
        Role role) {
}
