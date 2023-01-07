package com.project.project.forum.controller;

import com.project.project.forum.model.Post;
import com.project.project.forum.model.RequestPost;
import com.project.project.forum.model.Topic;
import com.project.project.forum.repository.PostRepository;
import com.project.project.forum.service.PostService;
import com.project.project.main.model.PostResponse;
import com.project.project.main.repository.PostAssessmentRepository;
import com.project.project.main.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostRepository postRepository;
    private final PostService postService;
    private final UserRepository userRepository;
    private final PostAssessmentRepository postAssessmentRepository;

    @GetMapping("/all")
    public ResponseEntity<List<Post>> getAllPosts() {

        var list = postRepository.findAll();
        Collections.sort(list);

        return ResponseEntity.ok(list);
    }

    @GetMapping("/topic/{id}")
    public ResponseEntity<List<PostResponse>> getAllPostsByTopic(@PathVariable UUID id) {
        return ResponseEntity.ok(postService.getPostsByTopic(id));
    }

    @GetMapping("/me")
    public ResponseEntity<List<Post>> getMePosts(Authentication authentication) {
        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        return ResponseEntity.ok(postRepository.findByUser(user));
    }

    @PostMapping("/add/{topicId}")
    public ResponseEntity<Topic> createPost(Authentication authentication, @PathVariable UUID topicId, @RequestBody RequestPost requestPost) {
        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        return new ResponseEntity<>(postService.createPost(topicId, requestPost, user), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/{type}/assess")
    public ResponseEntity<Post> realizeLikePost(Authentication authentication, @PathVariable UUID id, @PathVariable String type) {
        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        var post = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Post does not exist"));

        return ResponseEntity.ok(postService.postAssessmentRealization(post, user, type));
    }

    @DeleteMapping("/{id}/remove")
    public void removePost(@PathVariable UUID id){

        var post = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Post does not exist"));
        var postAssessmentList = postAssessmentRepository.findByPost(post);
        postAssessmentRepository.deleteAll(postAssessmentList);
        postRepository.delete(post);
    }
}