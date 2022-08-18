package ru.danis0n.avitoclone.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.danis0n.avitoclone.dto.Comment;
import ru.danis0n.avitoclone.dto.CommentRequest;
import ru.danis0n.avitoclone.entity.AppUserEntity;
import ru.danis0n.avitoclone.entity.CommentEntity;
import ru.danis0n.avitoclone.repository.CommentRepository;
import ru.danis0n.avitoclone.service.appuser.AppUserService;
import ru.danis0n.avitoclone.util.JwtUtil;
import ru.danis0n.avitoclone.util.ObjectMapperUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final ObjectMapperUtil objectMapperUtil;
    private final JwtUtil jwtUtil;
    private final AppUserService appUserService;
    private final CommentRepository commentRepository;

    @Override
    public String saveComment(CommentRequest comment, HttpServletRequest request) {
        CommentEntity commentEntity = objectMapperUtil.mapToCommentEntity(comment);

        String username = jwtUtil.getUsernameFromRequest(request);
        if(!username.equals(comment.getOwnerUsername())){
            return "You don't have enough permissions!";
        }
        commentRepository.save(commentEntity);
        return "Successful!";
    }

    @Override
    public String deleteComment(Long id, HttpServletRequest request) {
        CommentEntity commentEntity = commentRepository.findById(id).orElse(null);
        if(commentEntity == null){
            return "Null!";
        }

        String username = jwtUtil.getUsernameFromRequest(request);
        AppUserEntity user = appUserService.getAppUserEntity(username);

        if (commentEntity.getOwnerUser().equals(user) ||
                commentEntity.getUser().equals(user)){

            commentRepository.delete(commentEntity);
            return "Successful!";
        }
        return "You don't have enough permissions";
    }

    @Override
    public Comment getById(Long id) {
        CommentEntity commentEntity = commentRepository.findById(id).orElse(null);
        if(commentEntity == null){
            return null;
        }
        return objectMapperUtil.mapToComment(commentEntity);
    }

    @Override
    public List<Comment> getCommentsByUser(String username) {
        AppUserEntity user = appUserService.getAppUserEntity(username);
        List<CommentEntity> commentEntities = commentRepository.getByUser(user);
        List<Comment> comments = new ArrayList<>();
        for(CommentEntity entity : commentEntities){
            comments.add(objectMapperUtil.mapToComment(entity));
            log.info("+ Comment");
        }
        return comments;
    }

    @Override
    public List<Comment> getCommentsByOwnerUser(String username) {
        AppUserEntity user = appUserService.getAppUserEntity(username);
        List<CommentEntity> commentEntities = commentRepository.getByOwnerUser(user);
        List<Comment> comments = new ArrayList<>();
        for(CommentEntity entity : commentEntities){
            comments.add(objectMapperUtil.mapToComment(entity));
        }
        return comments;
    }

    @Override
    public List<Comment> getCommentsByUserId(Long id) {
        return null;
    }
}
