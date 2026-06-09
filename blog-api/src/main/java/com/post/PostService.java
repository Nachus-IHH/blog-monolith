package com.post;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.post.domain.Post;
import com.post.dto.CreatePostDto;
import com.post.dto.UpdatePostDto;
import com.post.dto.response.PostDetailDto;
import com.user.UsuarioRepository;
import com.user.domain.Usuario;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * PostService Service / Orquestador de lógica para el Post
 */
@AllArgsConstructor
@Slf4j
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Map Post to PostDetailDto
     * 
     * @param post Post a mapear
     * @return PostDetailDto
     */
    private PostDetailDto mapToPostDetailDto(Post post) {
        return new PostDetailDto(
                post.getId(),
                post.getTitulo(),
                post.getContent(),
                post.getFechaCreacion(),
                post.getUserWhoPost().getId());
    }

    /**
     * Creacion de un Post
     *
     * @param request CreatePostDto datos del post (title, content, idUser)
     * @return PostDetailDto regresa los detalles del post que se creo
     */
    @Transactional
    public PostDetailDto crearPost(CreatePostDto request) {

        log.info("Proceso de creacion de Post del usuario: {}", request.idUser());

        log.info("Buscando usuario...");
        Usuario usuario = usuarioRepository.findById(request.idUser())
                .orElseThrow(() -> {
                    log.error("Usuario no encontrado con ID: {}", request.idUser());
                    throw new RuntimeException("Usuario no encontrado");
                });

        Post post = Post.builder()
                .titulo(request.title())
                .content(request.content())
                .userWhoPost(usuario)
                .build();

        Post postSaved = postRepository.save(post);
        log.info("Post creado exitosamente con ID: {}", postSaved.getId());

        return mapToPostDetailDto(postSaved);
    }

    /**
     * Obtiene un post por ID
     *
     * @param id ID del post a buscar
     * @return PostDetailDto detalles del post buscado
     */
    @Transactional(readOnly = true)
    public PostDetailDto obtenerPost(Long id) {
        log.info("Buscando post con ID: {}", id);

        Post post = postRepository.findById(id).orElseThrow(() -> {
            log.error("Post no encontrado con ID: {}", id);
            throw new RuntimeException("Post no encontrado");
        });

        return mapToPostDetailDto(post);
    }

    /**
     * Obtiene una lista de todos los post (Transactional)
     *
     * @return List<PostDetailDto> Regresa una lista de todos los post encontrados
     */
    @Transactional(readOnly = true)
    public List<PostDetailDto> obtenerTodos() {
        log.info("Obteniendo lista de todos los posts");

        return postRepository.findAll().stream()
                .map(this::mapToPostDetailDto)
                .collect(Collectors.toList());
    }

    /**
     * Actualiza la informacion de un Post
     *
     * @param id      ID del post
     * @param request Datos a actualizar (title, content)
     * @return PostDetailDto Regresa los detalles del Post actualizado
     */
    @Transactional
    public PostDetailDto actualizarPost(Long id, UpdatePostDto request) {
        log.info("Iniciando actualizacion para el Post con ID: {}", id);

        Post postEncontrado = postRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Post no encontrado con ID: {}", id);
                    throw new RuntimeException("Post no encontrado");
                });

        /* Actualiza title */
        if (request.title() != null && !request.title().isBlank()) {
            log.debug("Actualizando titulo");
            postEncontrado.setTitulo(request.title());
        }

        /* Actualiza content */
        if (request.content() != null && !request.content().isBlank()) {
            log.debug("Actualizando contenido");
            postEncontrado.setContent(request.content());
        }

        Post postSaved = postRepository.save(postEncontrado);
        log.info("Post actualizado exitosamente con ID: {}", id);

        return mapToPostDetailDto(postSaved);
    }

    /**
     * Elimina un Post por ID (Transactional)
     *
     * @param id ID del post a eliminar
     */
    @Transactional
    public void eliminarPost(Long id) {
        log.info("Eliminando Post con ID: {}", id);

        if (!postRepository.existsById(id)) {
            log.error("Post no encontrado con ID: {}", id);
            throw new RuntimeException("Post no encontrado");
        }

        postRepository.deleteById(id);
    }
}
