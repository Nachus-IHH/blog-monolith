package com.comment;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.comment.domain.Comment;
import com.comment.dto.request.CreateCommentDto;
import com.comment.dto.request.UpdateCommentDto;
import com.comment.dto.response.DetailCommentDto;
import com.post.PostRepository;
import com.post.domain.Post;
import com.user.UsuarioRepository;
import com.user.domain.Usuario;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Map Comment to DetailCommentDto
     * 
     * @param comment Comment objeto que se va a mapear
     * @return DetailCommentDto Regresa el comentario en un DTO
     */
    private DetailCommentDto mapToDetailCommentDto(Comment comment) {
        return new DetailCommentDto(
                comment.getContent(),
                comment.getUsuario().getId(),
                comment.getPost().getId(),
                comment.getFechaCreacion());
    }

    /**
     * Crea un comentario (Transactional)
     *
     * @param request CreateCommentDto datos con los que se crea el comentario
     *                (content, idUsuario, idPost)
     * @return DetailCommentDto regresa los detalles del comentario
     */
    @Transactional
    public DetailCommentDto crearComentario(CreateCommentDto request) {
        log.info("Creando comentario del post: {} y usuario: {}", request.idPost(), request.idUser());

        Usuario usuario = usuarioRepository.findById(request.idUser())
                .orElseThrow(() -> {
                    log.error("Usuario no encontrado con ID: {}", request.idUser());
                    throw new RuntimeException("Usuario no encontrado. No se puede crear un comentario sin usuario");
                });

        Post post = postRepository.findById(request.idPost())
                .orElseThrow(() -> {
                    log.error("Post no encontrado con ID: {}", request.idPost());
                    throw new RuntimeException("Post no identificado, no se pudo crear el comentario");
                });

        Comment comment = Comment.builder()
                .content(request.content())
                .post(post)
                .usuario(usuario)
                .build();

        Comment commentSaved = commentRepository.save(comment);
        log.info("Comentario guardado exitosamente con ID: {}", commentSaved.getId());

        return mapToDetailCommentDto(commentSaved);
    }

    /**
     * Obtiene un comentario por su ID
     *
     * @param id del Comentario
     * @return DetailCommentDto regresa el Comment en DTO
     */
    @Transactional(readOnly = true)
    public DetailCommentDto getCommentById(Long id) {
        log.info("Obteniendo Comentario por ID: {}", id);

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Comentario no encontrado con ID: {}", id);
                    throw new RuntimeException("Comentario no encontrado");
                });

        return mapToDetailCommentDto(comment);
    }

    /**
     * Obtiene todos los comentarios de un Post
     *
     * @param idPost ID del post que se quieren obtener los comentarios
     * @return List<DetailCommentDto> Regresa una lista de todos los comentarios del
     *         Post
     */
    @Transactional(readOnly = true)
    public List<DetailCommentDto> getAllCommentsByPost(Long idPost) {
        log.info("Obteniendo lista de los comentarios del Post: {}", idPost);

        if (!postRepository.existsById(idPost)) {
            log.error("No se encontro el post con ID: {}", idPost);
            throw new RuntimeException("Post no encontrado");
        }

        return commentRepository.findByPostId(idPost).stream()
                .map(this::mapToDetailCommentDto)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todos los comentarios de un usuario
     *
     * @param idUsuario ID del Usuario del que se quieren obtener comentarios
     * @return List<DetailCommentDto> Regresa una lista de todos los comentarios del
     *         usuario
     */
    @Transactional(readOnly = true)
    public List<DetailCommentDto> getAllCommentsByUsuario(Long idUsuario) {
        log.info("Obteniendo lista de los comentarios del Usuario: {}", idUsuario);

        if (!usuarioRepository.existsById(idUsuario)) {
            log.error("Usuario no encontrado con ID: {}", idUsuario);
            throw new RuntimeException("Usuario no encontrado");
        }

        return commentRepository.findByUsuarioId(idUsuario).stream()
                .map(this::mapToDetailCommentDto)
                .collect(Collectors.toList());
    }

    /**
     * Actualiza el contenido de un comentario
     * 
     * @param idComment ID del comentario a actualizar
     * @param request   UpdateCommentDto contenido del comentario
     * @return DetailCommentDto Regresa el comentario actualizado en DTO
     */
    @Transactional
    public DetailCommentDto updateComment(Long idComment, UpdateCommentDto request) {
        log.info("Actualizando comentario con ID: {}", idComment);

        Comment commentFounded = commentRepository.findById(idComment)
                .orElseThrow(() -> {
                    log.error("No se encontro el comentario con ID: {}", idComment);
                    throw new RuntimeException("No se encontro el comentario que se quiere actualizar");
                });

        if (request.content() != null && !request.content().isBlank()) {
            log.debug("Actualizando contenido");
            commentFounded.setContent(request.content());
        }

        Comment commentSaved = commentRepository.save(commentFounded);
        log.info("Comentario: {} actualizado exitosamente", idComment);

        return mapToDetailCommentDto(commentSaved);
    }

    /**
     * Elimina un Comentario por ID
     * 
     * @param id ID del comentario a eliminar
     */
    public void deleteCommentById(Long id) {
        log.info("Eliminando comentario por ID: {}", id);

        if (!commentRepository.existsById(id)) {
            log.error("No se encontro comentario por ID: {}", id);
            throw new RuntimeException("No se encontro el comentario");
        }

        commentRepository.deleteById(id);
    }
}
