package ru.danis0n.avitoclone.service.comment;

import ru.danis0n.avitoclone.dto.comment.Comment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface CommentService {

    Comment getById(Long id);
    String saveComment(HttpServletRequest request, HttpServletResponse response);
    String deleteComment(Long id, HttpServletRequest request);
    List<Comment> getCommentsByUser(Long id);
    List<Comment> getCommentsByCreator(Long id);
    List<Comment> getCommentsByUserId(Long id);
}
