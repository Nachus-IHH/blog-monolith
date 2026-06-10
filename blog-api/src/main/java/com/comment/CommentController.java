package com.comment;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.comment.dto.request.CreateCommentDto;
import com.comment.dto.request.UpdateCommentDto;
import com.comment.dto.response.DetailCommentDto;
import com.config.ApiPaths;

import lombok.AllArgsConstructor;

@RequestMapping(ApiPaths.COMMENTS_PATH)
@RestController
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<DetailCommentDto> createComment(
            @RequestBody CreateCommentDto request) {

        return ResponseEntity.ok(commentService.crearComentario(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetailCommentDto> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(commentService.getCommentById(id));
    }

    /*
     * move to PostController ...
     * /post/{id}/comments
     */
    @GetMapping("/post/{id}")
    public ResponseEntity<List<DetailCommentDto>> getByPost(
            @PathVariable Long id) {

        return ResponseEntity.ok(commentService.getAllCommentsByPost(id));
    }

    /*
     * move to UsuarioController ...
     * /user/{id}/comments
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<List<DetailCommentDto>> getByUser(
            @PathVariable Long id) {

        return ResponseEntity.ok(commentService.getAllCommentsByUsuario(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DetailCommentDto> updateComment(
            @PathVariable Long id,
            @RequestBody UpdateCommentDto request) {

        return ResponseEntity.ok(commentService.updateComment(id, request));
    }

    @DeleteMapping("/{id}")
    public void deleteComment(
            @PathVariable Long id) {

        commentService.deleteCommentById(id);
    }
}
