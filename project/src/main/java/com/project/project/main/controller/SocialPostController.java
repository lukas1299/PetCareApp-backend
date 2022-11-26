package com.project.project.main.controller;

import com.project.project.main.exception.ObjectNotFoundException;
import com.project.project.main.model.SocialPost;
import com.project.project.main.model.SocialPostRequest;
import com.project.project.main.repository.ProfileRepository;
import com.project.project.main.repository.UserRepository;
import com.project.project.main.service.SocialPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/socialPost")
@RequiredArgsConstructor
public class SocialPostController {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final SocialPostService socialPostService;

    @GetMapping("/all")
    public ResponseEntity<List<SocialPost>> getUserSocialPosts(Authentication authentication){

        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        var profile = profileRepository.findByUserId(user.getId()).orElseThrow(() -> new ObjectNotFoundException("Profile does not exist"));

        return ResponseEntity.ok(profile.getSocialPosts());

    }

    @GetMapping("/friends/all")
    public ResponseEntity<List<SocialPost>> getFriendsPosts(Authentication authentication){

        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        var profile = profileRepository.findByUserId(user.getId()).orElseThrow(() -> new ObjectNotFoundException("Profile does not exist"));

        return ResponseEntity.ok(socialPostService.getFriendsPosts(profile));

    }

    @PostMapping("/add")
    public ResponseEntity<SocialPost> createSocialPost(Authentication authentication, @RequestBody SocialPostRequest socialPostRequest){

        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        return ResponseEntity.ok(socialPostService.createSocialPost(user, socialPostRequest));

    }

}
