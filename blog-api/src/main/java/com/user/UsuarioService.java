package com.user;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.user.domain.Role;
import com.user.domain.Usuario;
import com.user.dto.RequestUsuarioDto;
import com.user.dto.ResponseUsuarioDto;
import com.user.exception.EmailAlreadyExistsException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * UsuarioService
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Mapper Usuario to ResponseUsuarioDto
     * 
     * @param usuario Usuario que se mapeara
     * @return ResponseUsuarioDto
     */
    private ResponseUsuarioDto mapToDto(Usuario usuario) {
        return new ResponseUsuarioDto(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getRole());
    }

    /**
     * Registra un usuario en la DB (Transactional)
     * 
     * @param request Datos que se enviaron al end-point
     * @return ResponseUsuarioDto Regresa datos del usuario registrado
     */
    @Transactional
    public URI crearUsuario(RequestUsuarioDto request) {
        log.info("Proceso de creacion del usuario: {}", request.username());

        /* Verificar correo electronico */
        log.debug("Verificando correo electronico del solicitante: {}", request.email());
        if (usuarioRepository.existsByEmail(request.email())) {
            log.error("El email {} ya existe", request.email());
            throw new EmailAlreadyExistsException("Este email ya esta en uso");
        }

        String passwordEncriptada = passwordEncoder.encode(request.password());
        log.debug("Contraseña encriptada correctamente con BCrypt");

        Usuario usuario = Usuario.builder()
                .username(request.username())
                .password(passwordEncriptada)
                .email(request.email())
                .role(Role.USER)
                .build();

        Usuario usuarioSaved = usuarioRepository.save(usuario);
        log.info("Usuario creado exitosamente con ID: {}", usuarioSaved.getId());

        return mapToDto(usuarioSaved);
    }

    /**
     * Obtiene un Usuario por su ID (Transactional)
     * 
     * @param id ID del usuario a buscar
     * @return ResponseUsuarioDto Regresa datos del usuario obtenido
     */
    @Transactional(readOnly = true)
    public ResponseUsuarioDto obtenerUsuario(Long id) {
        log.info("Buscando usuario con ID: {}", id);

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("No se encontró el usuario con ID: {}", id);
                    return new RuntimeException("Usuario no encontrado");
                });

        return mapToDto(usuario);
    }

    /**
     * Regresa una lista de todos los Usuarios (Transactional)
     * 
     * @return List<ResponseUsuarioDto> Regresa la lista de usuarios
     */
    @Transactional(readOnly = true)
    public List<ResponseUsuarioDto> buscarTodos() {
        log.info("Obteniendo lista de todos los usuarios.");

        return usuarioRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Actualiza la información de un usuario (excepto el rol)
     * por su ID (Transactional)
     *
     * @param id      ID del usuario a actualizar
     * @param request Datos que se quieren actualizar
     * @return ResponseUsuarioDto Regresa los datos del usuario actualizado
     */
    @Transactional
    public ResponseUsuarioDto actualizarUsuario(Long id, RequestUsuarioDto request) {
        log.info("Iniciando actualización para el usuario con ID: {}", id);

        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Error al actualizar: No se encontro el usuario con ID: {}", id);
                    return new RuntimeException("Usuario no encontrado");
                });

        /* Actualiza username */
        if (request.username() != null && !request.username().isBlank()) {
            log.debug("Actualizando username de '{}' a '{}'", usuarioExistente.getUsername(), request.username());
            usuarioExistente.setUsername(request.username());
        }

        /* Actualiza email */
        if (request.email() != null && !request.email().isBlank()) {
            log.debug("Actualizando email de '{}' a '{}'", usuarioExistente.getEmail(), request.email());
            usuarioExistente.setEmail(request.email());
        }

        /* Actualiza password */
        if (request.password() != null && !request.password().isBlank()) {
            log.debug("Detectada nueva password. Encriptando");
            String passwordEncriptada = passwordEncoder.encode(request.password());
            usuarioExistente.setPassword(passwordEncriptada);
        }

        Usuario usuarioActualizado = usuarioRepository.save(usuarioExistente);
        log.info("Usuario con ID: {} actualizado exitosamente", usuarioActualizado.getId());

        return mapToDto(usuarioActualizado);
    }

    /**
     * Elimina a un Usuario por su ID (Transactional)
     * 
     * @param id ID del usuario a eliminar
     */
    @Transactional
    public void eliminarUsuario(Long id) {
        log.info("Solicitud para eliminar usuario con ID: {}", id);

        if (!usuarioRepository.existsById(id)) {
            log.error("Error al eliminar: No existe el usuario con ID: {}", id);
            throw new RuntimeException("No se puede eliminar. Usuario no encontrado");
        }

        usuarioRepository.deleteById(id);
        log.info("Usuario eliminado con ID: {}", id);
    }

}
