package com.project.project.forum.controller;

import com.project.project.forum.model.Post;
import com.project.project.forum.model.RequestPost;
import com.project.project.forum.model.Topic;
import com.project.project.forum.repository.PostRepository;
import com.project.project.forum.repository.TopicRepository;
import com.project.project.forum.service.PostService;
import com.project.project.main.model.User;
import com.project.project.main.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostRepository postRepository;
    private final PostService postService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts(){
        return ResponseEntity.ok(postRepository.findAll());
    }

    @GetMapping("/topic/{id}")
    public ResponseEntity<List<Post>> getAllPostsByTopic(@PathVariable Long id){

        return ResponseEntity.ok(postService.getPostsByTopic(id));
    }

    @PostMapping("/add/{topicId}")
    public ResponseEntity<Topic> createPost(@PathVariable Long topicId, @RequestBody RequestPost requestPost){

        return new ResponseEntity<>(postService.createPost(topicId, requestPost), HttpStatus.CREATED);
    }

    @PostMapping("/like/{id}")
    public ResponseEntity<Post> realizeLikePost(@PathVariable Long id){

        //TODO get logged user
        var user = userRepository.findById(UUID.fromString("8355fa25-d7c4-4963-9704-6eab27553e8d")).orElseThrow(() -> new EntityNotFoundException("User does not exists"));
        var post = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Post does not exist"));

        return ResponseEntity.ok(postService.realizeLikePost(post, user));
    }

    @PostMapping("/dislike/{id}")
    public ResponseEntity<Post> realizeDislikePost(@PathVariable Long id){

        var user = userRepository.findById(UUID.fromString("8355fa25-d7c4-4963-9704-6eab27553e8d")).orElseThrow(() -> new EntityNotFoundException("User does not exists"));
        var post = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Post does not exist"));
        return ResponseEntity.ok(postService.realizeDislikePost(post, user));
    }

}