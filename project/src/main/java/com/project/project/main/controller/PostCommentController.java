package com.project.project.main.controller;

import com.project.project.main.model.PostComment;
import com.project.project.main.model.PostCommentRequest;
import com.project.project.main.repository.SocialPostRepository;
import com.project.project.main.service.PostCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class PostCommentController {

    private final PostCommentService postCommentService;
    private final SocialPostRepository socialPostRepository;

    @GetMapping("/{id}/get")
    public ResponseEntity<List<PostComment>> getPostComment(@PathVariable UUID id){

        var post = socialPostRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Post does not exist"));
        return ResponseEntity.ok(post.getPostCommentList());
    }

    @PostMapping("/{id}/add")
    public ResponseEntity<PostComment> addCommentToPost(@PathVariable UUID id, @RequestBody PostCommentRequest postCommentRequest){
        return ResponseEntity.ok(postCommentService.addCommentToPost(id, postCommentRequest));
    }
}
