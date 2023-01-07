package com.project.project.main.controller;

import com.project.project.main.model.PostComment;
import com.project.project.main.model.PostCommentRequest;
import com.project.project.main.model.SocialPost;
import com.project.project.main.repository.SocialPostRepository;
import com.project.project.main.repository.UserRepository;
import com.project.project.main.service.PostCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/comments")
@RequiredArgsConstructor
public class PostCommentController {

    private final PostCommentService postCommentService;
    private final SocialPostRepository socialPostRepository;
    private final UserRepository userRepository;

    @GetMapping("/{id}/get")
    public ResponseEntity<List<PostComment>> getPostComment(@PathVariable UUID id){

        var post = socialPostRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Post does not exist"));
        var list = post.getPostCommentList().stream().sorted(Comparator.comparing(PostComment::getDate)).toList();
        return ResponseEntity.ok(list);
    }

    @PostMapping("/{id}/add")
    public ResponseEntity<PostComment> addCommentToPost(Authentication authentication,  @PathVariable UUID id, @RequestBody PostCommentRequest postCommentRequest){
        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        return ResponseEntity.ok(postCommentService.addCommentToPost(id, postCommentRequest, user));
    }
}
