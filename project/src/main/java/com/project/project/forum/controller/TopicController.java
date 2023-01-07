package com.project.project.forum.controller;

import com.project.project.forum.model.RequestTopic;
import com.project.project.forum.model.Topic;
import com.project.project.forum.model.TopicResponse;
import com.project.project.forum.repository.TopicRepository;
import com.project.project.forum.service.TopicService;
import com.project.project.main.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/topics")
@RequiredArgsConstructor
public class TopicController {

    private final TopicRepository topicRepository;
    private final UserRepository userRepository;
    private final TopicService topicService;

    @GetMapping
    public ResponseEntity<List<TopicResponse>> getAllTopics() {
        var list = topicRepository.findAll();

        Collections.sort(list);

        var finalList = list.stream()
                .map(topic -> {
                    var t = topicRepository.findById(topic.getId()).get();
                    return new TopicResponse(t, topic.getUser().getUsername(), topic.getUser().getPhoto());
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(finalList);
    }

    @PostMapping("/add")
    public ResponseEntity<Topic> addTopic(@RequestBody RequestTopic requestTopic, Authentication authentication) {

        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        return ResponseEntity.ok(topicService.createTopic(user, requestTopic));
    }
}
