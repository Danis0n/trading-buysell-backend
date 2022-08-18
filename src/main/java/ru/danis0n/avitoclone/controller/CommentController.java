package ru.danis0n.avitoclone.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.danis0n.avitoclone.dto.Comment;
import ru.danis0n.avitoclone.dto.CommentRequest;
import ru.danis0n.avitoclone.service.comment.CommentService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/save")
    public ResponseEntity<String> create(@ModelAttribute CommentRequest comment, HttpServletRequest request){
        return ResponseEntity.ok().body(commentService.saveComment(comment,request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id, HttpServletRequest request){
        return ResponseEntity.ok().body(commentService.deleteComment(id,request));
    }

    @GetMapping("/get/user/{id}")
    public ResponseEntity<List<Comment>> getComments(@PathVariable String id){
        return ResponseEntity.ok().body(commentService.getCommentsByUser(id));
    }

    @GetMapping("/get/user/own/{id}")
    public ResponseEntity<List<Comment>> getOwnerComments(@PathVariable String id){
        return ResponseEntity.ok().body(commentService.getCommentsByOwnerUser(id));
    }

}
