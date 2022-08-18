package ru.danis0n.avitoclone.service.comment;

import ru.danis0n.avitoclone.dto.Comment;
import ru.danis0n.avitoclone.dto.CommentRequest;

import java.util.List;

public interface CommentService {

    Comment getById(Long id);
    String deleteComment(Long id);
    String saveComment(CommentRequest comment);
    List<Comment> getCommentsByUser(String username);
    List<Comment> getCommentsByOwnerUser(String username);
    List<Comment> getCommentsByUserId(Long id);
}
