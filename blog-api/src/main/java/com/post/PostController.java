package com.post;

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
import com.post.dto.request.CreatePostDto;
import com.post.dto.request.UpdatePostDto;
import com.post.dto.response.PostDetailDto;

import lombok.AllArgsConstructor;

/**
 * PostController Controller / Endpoints
 */
@AllArgsConstructor
@RequestMapping(ApiPaths.BLOG_PATH)
@RestController
public class PostController {

    private final PostService postService;

    /**
     * Endpoint para crear un Post
     *
     * @param request CreatePostDto Datos para crear el Post (title, content,
     *                userId)
     * @return PostDetailDto Regresa detalles del post
     */
    @PostMapping
    public ResponseEntity<PostDetailDto> crearPost(
            @RequestBody CreatePostDto request) {
        return ResponseEntity.ok(
                postService.crearPost(request));
    }

    /**
     * Endpoint para obtener un post por ID
     *
     * @param id ID del post
     * @return PostDetailDto Regresa detalles del post
     */
    @GetMapping("/{id}")
    public ResponseEntity<PostDetailDto> obtenerPost(
            @PathVariable Long id) {
        return ResponseEntity.ok(postService.obtenerPost(id));
    }

    /**
     * Endpoint para obtener todos los Post
     *
     * @return List<PostDetailDto> Regresa todos los post encontrados
     */
    @GetMapping
    public ResponseEntity<List<PostDetailDto>> obtenerTodos() {
        return ResponseEntity.ok(postService.obtenerTodos());
    }

    /**
     * Endpoint para actualizar un Post
     *
     * @param id      ID del post
     * @param request UpdatePostDto Datos para actualizar el post (title, content)
     * @return PostDetailDto Regresa detalles del post
     */
    @PutMapping("/{id}")
    public ResponseEntity<PostDetailDto> actualizarPost(
            @PathVariable Long id,
            @RequestBody UpdatePostDto request) {
        return ResponseEntity.ok(postService.actualizarPost(id, request));
    }

    /**
     * Endpoint para eliminar un Post
     *
     * @param id ID del post
     */
    @DeleteMapping("/{id}")
    public void eliminarPost(
            @PathVariable Long id) {
        postService.eliminarPost(id);
    }

}
