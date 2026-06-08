package com.user;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.config.ApiPaths;
import com.user.dto.RequestUsuarioDto;
import com.user.dto.ResponseUsuarioDto;

import lombok.RequiredArgsConstructor;

/**
 * UsuarioController Endpoints de Usuario
 */
@RestController
@RequestMapping(ApiPaths.USER_PATH)
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    /**
     * Crea un nuevo Usuario
     * 
     * @param request RequestUsuarioDto (username, password, email)
     * @return ResponseUsuarioDto regresa todos los campos de usuario
     */
    @PostMapping
    public ResponseEntity<ResponseUsuarioDto> crearUsuario(
            @RequestBody RequestUsuarioDto request) {
        return ResponseEntity.ok(
                usuarioService.crearUsuario(request));
    }

    /**
     * Regresa el usuario buscado por id
     * 
     * @param id ID del usuario a buscar
     * @return ResponseUsuarioDto regresa todos los datos del usuario a excepción
     *         de la contraseña
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResponseUsuarioDto> obtenerUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(
                usuarioService.obtenerUsuario(id));
    }

    /**
     * Busca todos los usuarios
     * 
     * @return List<ResponseUsuarioDto> regresa la lista de usuarios
     */
    @GetMapping
    public ResponseEntity<List<ResponseUsuarioDto>> buscarTodos() {
        return ResponseEntity.ok(
                usuarioService.buscarTodos());
    }

    /**
     * Actualiza la información de un Usuario
     * 
     * @param id      ID del usuario a actualizar
     * @param request Datos a actualizar
     * @return ResponseUsuarioDto regresa datos del usuario
     */
    @PutMapping("/{id}")
    public ResponseEntity<ResponseUsuarioDto> actualizarUsuario(
            @PathVariable Long id,
            @RequestBody RequestUsuarioDto request) {
        return ResponseEntity.ok(
                usuarioService.actualizarUsuario(id, request));
    }

    /**
     * Elimina un usuario específico por su ID
     * 
     * @param id ID del usuario a eliminar
     * @return Long ID del usuario que se elimino
     */
    @DeleteMapping("/{id}")
    public void eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
    }

}
