package com.project.project.forum.controller;

import com.project.project.forum.model.Post;
import com.project.project.forum.model.RequestPost;
import com.project.project.forum.model.Topic;
import com.project.project.forum.repository.PostRepository;
import com.project.project.forum.service.PostService;
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

    @GetMapping("/all")
    public ResponseEntity<List<Post>> getAllPosts() {

        var list = postRepository.findAll();
        Collections.sort(list);

        return ResponseEntity.ok(list);
    }

    @GetMapping("/topic/{id}")
    public ResponseEntity<List<Post>> getAllPostsByTopic(@PathVariable UUID id) {

        return ResponseEntity.ok(postService.getPostsByTopic(id));
    }

    @GetMapping("/me")
    public ResponseEntity<List<Post>> getMePosts(Authentication authentication) {
        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        return ResponseEntity.ok(postRepository.findByUser(user));
    }

    @PostMapping("/add/{topicId}")
    public ResponseEntity<Topic> createPost(@PathVariable UUID topicId, @RequestBody RequestPost requestPost) {
        return new ResponseEntity<>(postService.createPost(topicId, requestPost), HttpStatus.CREATED);
    }

    @PostMapping("/like/{id}")
    public ResponseEntity<Post> realizeLikePost(@PathVariable UUID id, Authentication authentication) {
        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        var post = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Post does not exist"));

        return ResponseEntity.ok(postService.realizeLikePost(post, user));
    }

    @PostMapping("/dislike/{id}")
    public ResponseEntity<Post> realizeDislikePost(@PathVariable UUID id, Authentication authentication) {
        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        var post = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Post does not exist"));

        return ResponseEntity.ok(postService.realizeDislikePost(post, user));
    }

}