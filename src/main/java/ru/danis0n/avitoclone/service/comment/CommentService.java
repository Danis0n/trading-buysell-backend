package ru.danis0n.avitoclone.service.comment;

import ru.danis0n.avitoclone.dto.Comment;
import ru.danis0n.avitoclone.dto.CommentRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CommentService {

    Comment getById(Long id);
    String deleteComment(Long id, HttpServletRequest request);
    String saveComment(CommentRequest comment, HttpServletRequest request);
    List<Comment> getCommentsByUser(String username);
    List<Comment> getCommentsByOwnerUser(String username);
    List<Comment> getCommentsByUserId(Long id);
}
