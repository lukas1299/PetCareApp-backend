package com.project.project.forum.service;

import com.project.project.forum.model.Post;
import com.project.project.forum.model.RequestPost;
import com.project.project.forum.model.Topic;
import com.project.project.forum.repository.PostRepository;
import com.project.project.forum.repository.TopicRepository;
import com.project.project.main.exception.AssessmentException;
import com.project.project.main.model.*;
import com.project.project.main.repository.PostAssessmentRepository;
import com.project.project.main.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final TopicRepository topicRepository;
    private final PostRepository postRepository;
    private final PostAssessmentRepository postAssessmentRepository;

    public List<PostResponse> getPostsByTopic(UUID id) {

        var topic = topicRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Topic does not exists"));
        var list = postRepository.findByTopic(topic);

        var resultList = new ArrayList<PostResponse>();

        Collections.sort(list);

        list.forEach(post -> resultList.add(new PostResponse(post, post.getUser().getUsername())));

        return resultList;
    }

    public Topic createPost(UUID topicId, RequestPost requestPost, User user) throws PersistenceException {

        var topic = topicRepository.findById(topicId).orElseThrow(() -> new EntityNotFoundException("Topic does not exist"));

        var post = Post.fromDto(requestPost, getCurrentDateTime(), user);
        post.setTopic(topic);

        try {
            postRepository.save(post);
        } catch (Exception e) {
            throw new PersistenceException("Error saving post", e);
        }

        topic.addPost(post);

        try {
            return topicRepository.save(topic);
        } catch (Exception e) {
            throw new PersistenceException("Error updating topic", e);
        }
    }

    public Post postAssessmentRealization(Post post, User user, String assessmentType) {

        var result = postAssessmentRepository.findByPostAndUser(post, user);

        if (!result.isEmpty()) {
            throw new AssessmentException();
        }

        var postResult = postRepository.findById(post.getId()).orElseThrow(() -> new EntityNotFoundException("Social post does not exist"));

        switch (assessmentType) {
            case "like" -> postResult.setPositiveOpinionAmount(postResult.getPositiveOpinionAmount() + 1);
            case "dislike" -> postResult.setNegativeOpinionAmount(postResult.getNegativeOpinionAmount() + 1);
        }

        postAssessmentRepository.save(PostAssessment.fromDto(user, postResult));

        return postResult;
    }

    private String getCurrentDateTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return localDateTime.format(format);
    }
}
