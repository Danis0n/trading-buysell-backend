package ru.danis0n.avitoclone.service.comment;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.danis0n.avitoclone.dto.Comment;
import ru.danis0n.avitoclone.dto.CommentRequest;
import ru.danis0n.avitoclone.dto.RegistrationRequest;
import ru.danis0n.avitoclone.entity.AppUserEntity;
import ru.danis0n.avitoclone.entity.CommentEntity;
import ru.danis0n.avitoclone.repository.CommentRepository;
import ru.danis0n.avitoclone.service.appuser.AppUserService;
import ru.danis0n.avitoclone.util.JsonUtil;
import ru.danis0n.avitoclone.util.JwtUtil;
import ru.danis0n.avitoclone.util.ObjectMapperUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final ObjectMapperUtil objectMapperUtil;
    private final JwtUtil jwtUtil;
    private final JsonUtil jsonUtil;
    private final AppUserService appUserService;
    private final CommentRepository commentRepository;

    @Override
    public String saveComment(HttpServletRequest request, HttpServletResponse response) {

        CommentRequest comment = new Gson().fromJson(
                jsonUtil.getJson(request),
                CommentRequest.class
        );

        String username = jwtUtil.getUsernameFromRequest(request);
        if(!username.equals(comment.getCreatedBy())){
            return "You don't have enough permissions!";
        }

        CommentEntity commentEntity = objectMapperUtil.mapToCommentEntity(comment);
        countRating(commentEntity);

        commentRepository.save(commentEntity);
        return "Successful!";
    }

    // TODO : fill it with code
    @Override
    public String updateComment(Long id, HttpServletRequest request) {
        return null;
    }

    public void countRating(CommentEntity comment){
        AppUserEntity user = comment.getTo();
        float prevRating = user.getUserInfo().getRating();

        if(prevRating == 0.0){
            user.getUserInfo().setRating(comment.getRating());
            return;
        }

        float count = commentRepository.getByTo(user).size() + 1;
        float rating = (prevRating * count + comment.getRating()) / (count + 1);
        user.getUserInfo().setRating(rating);
        comment.setTo(user);
    }

    @Override
    public String deleteComment(Long id, HttpServletRequest request) {
        CommentEntity commentEntity = commentRepository.findById(id).orElse(null);
        if(commentEntity == null){
            return "Null!";
        }

        String username = jwtUtil.getUsernameFromRequest(request);
        AppUserEntity user = appUserService.getAppUserEntity(username);

        if (commentEntity.getCreatedBy().equals(user) ||
                commentEntity.getTo().equals(user)){

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
        List<CommentEntity> commentEntities = commentRepository.getByTo(user);
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
        List<CommentEntity> commentEntities = commentRepository.getByCreatedBy(user);
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
