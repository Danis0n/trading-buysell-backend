package ru.danis0n.avitoclone.service.comment;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.danis0n.avitoclone.dto.comment.Comment;
import ru.danis0n.avitoclone.dto.comment.CommentRequest;
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
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final ObjectMapperUtil objectMapperUtil;
    private final JwtUtil jwtUtil;
    private final JsonUtil jsonUtil;
    private final AppUserService appUserService;
    private final CommentRepository commentRepository;

    // TODO : refactor this

    @Override
    public String saveComment(HttpServletRequest request, HttpServletResponse response) {

        CommentRequest comment = getCommentFromJson(request);
        AppUserEntity user = getAppUserEntityFromString(getUsername(request));
        log.info(comment.toString());
        if(!validateUser(
                user.getId().toString(), comment.getCreatedBy())) {
            return "You don't have enough permissions!";
        }

        CommentEntity commentEntity = getCommentEntityFromCommentRequest(comment);
        countRating(commentEntity);
        saveComment(commentEntity);
        return "Successful!";
    }

    @Override
    public String updateComment(Long id, HttpServletRequest request, HttpServletResponse response) {

        CommentRequest comment = getCommentFromJson(request);
//        AppUserEntity user = getAppUserEntityFromString(getUsername(request));

        if(!validateUser(
                getUsername(request), comment.getCreatedBy())) {
            return "You don't have enough permissions!";
        }

        CommentEntity commentEntity = getCommentEntityById(id);
        if(commentEntity == null){
            return "Not found";
        }

        commentEntity = getCommentEntityFromCommentRequest(comment);
        saveComment(commentEntity);
        return "Successful";
    }

    @Override
    public String deleteComment(Long id, HttpServletRequest request) {

        CommentEntity commentEntity = getCommentEntityById(id);
        if(commentEntity == null){
            return "Null!";
        }

        AppUserEntity editor = appUserService.getAppUserEntityByUsername(getUsername(request));
        AppUserEntity creator = appUserService.getAppUserEntityById(commentEntity.getCreatedBy().getId());
        AppUserEntity to = appUserService.getAppUserEntityById(commentEntity.getTo().getId());

        if(validateUser(creator.getUsername(), editor.getUsername()) ||
                validateUser(to.getUsername(), editor.getUsername())) {
            commentRepository.delete(commentEntity);
            return "Successful!";
        }

        return "You don't have enough permissions";
    }

    @Override
    public Comment getById(Long id) {
        CommentEntity commentEntity = getCommentEntityById(id);
        if(commentEntity == null){
            return null;
        }
        return getCommentFromCommentEntity(commentEntity);
    }

    @Override
    public List<Comment> getCommentsByUser(Long id) {
        AppUserEntity user = appUserService.getAppUserEntityById(id);
        List<CommentEntity> commentEntities = commentRepository.getByTo(user);

        List<Comment> comments = new ArrayList<>();
        for(CommentEntity entity : commentEntities){
            comments.add(getCommentFromCommentEntity(entity));
        }
        return comments;
    }

    @Override
    public List<Comment> getCommentsByCreator(String username) {
        AppUserEntity user = getAppUserEntityFromString(username);
        List<CommentEntity> commentEntities = commentRepository.getByCreatedBy(user);

        List<Comment> comments = new ArrayList<>();
        for(CommentEntity entity : commentEntities){
            comments.add(getCommentFromCommentEntity(entity));
        }
        return comments;
    }

    @Override
    public List<Comment> getCommentsByUserId(Long id) {
        return null;
    }

    private void countRating(CommentEntity comment){
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

    private CommentRequest getCommentFromJson(HttpServletRequest request){
        return jsonUtil.getGson().fromJson(
                jsonUtil.getJson(request),
                CommentRequest.class
        );
    }

    private boolean validateUser(String commentCreator, String commentEditor){
        return commentCreator.equals(commentEditor);
    }

    private String getUsername(HttpServletRequest request){
        return jwtUtil.getUsernameFromRequest(request);
    }

    private CommentEntity getCommentEntityFromCommentRequest(CommentRequest comment){
        return objectMapperUtil.mapToCommentEntity(comment);
    }

    private Comment getCommentFromCommentEntity(CommentEntity comment){
        return objectMapperUtil.mapToComment(comment);
    }

    private AppUserEntity getAppUserEntityFromString(String username){
        return appUserService.getAppUserEntityByUsername(username);
    }

    private CommentEntity getCommentEntityById(Long id){
        return commentRepository.findById(id).orElse(null);
    }

    private void saveComment(CommentEntity comment){
        commentRepository.save(comment);
    }
}
