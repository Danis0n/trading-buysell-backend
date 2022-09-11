package ru.danis0n.avitoclone.service.comment;

import ru.danis0n.avitoclone.dto.Comment;
import ru.danis0n.avitoclone.dto.CommentRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface CommentService {

    Comment getById(Long id);
    String saveComment(HttpServletRequest request, HttpServletResponse response);
    String updateComment(Long id, HttpServletRequest request);
    String deleteComment(Long id, HttpServletRequest request);
    List<Comment> getCommentsByUser(String username);
    List<Comment> getCommentsByOwnerUser(String username);
    List<Comment> getCommentsByUserId(Long id);
}
