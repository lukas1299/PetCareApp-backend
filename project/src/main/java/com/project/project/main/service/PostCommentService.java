package com.project.project.main.service;

import com.project.project.main.model.PostComment;
import com.project.project.main.model.PostCommentRequest;
import com.project.project.main.model.User;
import com.project.project.main.repository.PostCommentRepository;
import com.project.project.main.repository.SocialPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostCommentService {

    private final PostCommentRepository postCommentRepository;
    private final SocialPostRepository socialPostRepository;

    public PostComment addCommentToPost(UUID id, PostCommentRequest postCommentRequest, User user) {

        var post = socialPostRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Post does not exist"));
        var comment = PostComment.fromDto(postCommentRequest, user);
        comment.setSocialPost(post);
        postCommentRepository.save(comment);
        post.getPostCommentList().add(comment);

        return comment;
    }
}
