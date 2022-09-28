package ru.danis0n.avitoclone.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.danis0n.avitoclone.dto.Comment;
import ru.danis0n.avitoclone.service.comment.CommentService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;
    // TODO : FIX COMMENTS IDS
    @PostMapping("/save")
    public ResponseEntity<String> create(HttpServletRequest request, HttpServletResponse response){
        return ResponseEntity.ok().body(commentService.saveComment(request, response));
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response){
        return ResponseEntity.ok().body(commentService.updateComment(id,request,response));
    }
    // TODO : add response
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id, HttpServletRequest request){
        return ResponseEntity.ok().body(commentService.deleteComment(id,request));
    }

    @GetMapping("/get/user/{id}")
    public ResponseEntity<List<Comment>> getComments(@PathVariable Long id){
        return ResponseEntity.ok().body(commentService.getCommentsByUser(id));
    }
    // TODO : FIX {ID}
    @GetMapping("/get/user/own/{id}")
    public ResponseEntity<List<Comment>> getOwnerComments(@PathVariable String id){
        return ResponseEntity.ok().body(commentService.getCommentsByCreator(id));
    }

}
