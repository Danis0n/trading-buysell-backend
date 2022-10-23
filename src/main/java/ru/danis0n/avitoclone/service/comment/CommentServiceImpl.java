package ru.danis0n.avitoclone.service.comment;

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
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final ObjectMapperUtil mapperUtil;
    private final JwtUtil jwtUtil;
    private final JsonUtil jsonUtil;
    private final AppUserService appUserService;
    private final CommentRepository commentRepository;

    @Override
    public String saveComment(HttpServletRequest request, HttpServletResponse response) {

        CommentRequest comment = getCommentFromJson(request);
        AppUserEntity user = getAppUserEntityByUsername(getUsernameFromRequest(request));
        log.info(comment.toString());
        if(!validateUser(user.getId(), comment.getCreatedBy())) {
            return "You don't have enough permissions!";
        }

        CommentEntity commentEntity = getCommentEntityFromCommentRequest(comment);
        countRating(commentEntity);
        saveComment(commentEntity);
        return "Successful!";
    }

    @Override
    public String deleteComment(Long id, HttpServletRequest request) {

        CommentEntity commentEntity = getCommentEntityById(id);
        if(commentEntity == null){
            return "Null!";
        }

        Long editorId = appUserService.getAppUserEntityByUsername(getUsernameFromRequest(request)).getId();
        Long creatorId = commentEntity.getCreatedBy().getId();
        Long toId = commentEntity.getTo().getId();

        if(validateUser(creatorId, editorId) || validateUser(toId, editorId)) {
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
        AppUserEntity user = getAppUserEntityById(id);
        List<CommentEntity> commentEntities = commentRepository.getByTo(user);
        return mapListToComments(commentEntities);
    }

    @Override
    public List<Comment> getCommentsByCreator(Long id) {
        AppUserEntity user = getAppUserEntityById(id);
        List<CommentEntity> commentEntities = commentRepository.getByCreatedBy(user);
        return mapListToComments(commentEntities);
    }

    @Override
    public List<Comment> getCommentsByUserId(Long id) {
        return null;
    }

    private List<Comment> mapListToComments(List<CommentEntity> commentEntities) {
        return mapperUtil.mapListToComments(commentEntities);
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

    private boolean validateUser(Long commentCreator, Long commentEditor){
        return commentCreator.equals(commentEditor);
    }

    private String getUsernameFromRequest(HttpServletRequest request){
        return jwtUtil.getUsernameFromRequest(request);
    }

    private CommentEntity getCommentEntityFromCommentRequest(CommentRequest comment){
        return mapperUtil.mapToCommentEntity(comment);
    }

    private Comment getCommentFromCommentEntity(CommentEntity comment){
        return mapperUtil.mapToComment(comment);
    }

    private AppUserEntity getAppUserEntityByUsername(String username){
        return appUserService.getAppUserEntityByUsername(username);
    }

    private CommentEntity getCommentEntityById(Long id){
        return commentRepository.findById(id).orElse(null);
    }

    private void saveComment(CommentEntity comment){
        commentRepository.save(comment);
    }

    private AppUserEntity getAppUserEntityById(Long id){
        return appUserService.getAppUserEntityById(id);
    }

}
