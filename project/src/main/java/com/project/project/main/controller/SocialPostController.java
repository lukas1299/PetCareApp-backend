package com.project.project.main.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.project.main.exception.ObjectNotFoundException;
import com.project.project.main.model.SocialPost;
import com.project.project.main.model.SocialPostRequest;
import com.project.project.main.model.SocialPostResponse;
import com.project.project.main.repository.ProfileRepository;
import com.project.project.main.repository.SocialPostRepository;
import com.project.project.main.repository.UserRepository;
import com.project.project.main.service.SocialPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/socialPost")
@CrossOrigin
@RequiredArgsConstructor
public class SocialPostController {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final SocialPostService socialPostService;
    private final SocialPostRepository socialPostRepository;
    private final ObjectMapper objectMapper;

    @GetMapping("/all")
    public ResponseEntity<List<SocialPost>> getUserSocialPosts(Authentication authentication) {

        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        var profile = profileRepository.findByUserId(user.getId()).orElseThrow(() -> new ObjectNotFoundException("Profile does not exist"));

        var list = socialPostRepository.findByProfile(profile).get().stream().sorted().toList();

        return ResponseEntity.ok(list);
    }

    @GetMapping("/friends/all")
    public ResponseEntity<List<SocialPostResponse>> getFriendsPosts(Authentication authentication) {

        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        var profile = profileRepository.findByUserId(user.getId()).orElseThrow(() -> new ObjectNotFoundException("Profile does not exist"));

        var socialPostsList = socialPostService.getFriendsPosts(profile);
        var fullList = new ArrayList<SocialPostResponse>();

        socialPostsList.sort(Collections.reverseOrder());

        socialPostsList.forEach(socialPost -> fullList.add(new SocialPostResponse(socialPost, socialPost.getProfile().getUser())));

        return ResponseEntity.ok(fullList);
    }

    @PostMapping("/add")
    public ResponseEntity<SocialPost> createSocialPost(@RequestParam(value = "file", required = false) MultipartFile file, @RequestParam("json") String json, Authentication authentication) throws IOException {

        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        var socialPostRequest = objectMapper.readValue(json, SocialPostRequest.class);
        return ResponseEntity.ok(socialPostService.createSocialPost(user, socialPostRequest, file));
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getSocialPostImage(@PathVariable UUID id) {
        var socialPost = socialPostRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Social post does not exists"));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + socialPost.getContent() + "\"")
                .contentType(MediaType.IMAGE_JPEG)
                .body(socialPost.getPhoto());
    }

    @PostMapping("/{id}/{type}/assess")
    public ResponseEntity<SocialPost> socialPostLike(Authentication authentication, @PathVariable UUID id, @PathVariable String type) {

        var socialPost = socialPostRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Social post does not exist"));
        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));

        var socialPostResult = socialPostService.assessmentRealization(user, socialPost, type);

        return ResponseEntity.ok(socialPostResult);
    }

}
