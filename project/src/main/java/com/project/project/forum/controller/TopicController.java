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

import javax.persistence.EntityNotFoundException;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
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
        var list = topicRepository.findAll().stream().sorted(Comparator.comparing(Topic::getCreationDate).reversed()).toList();

        var finalList = list.stream()
                .map(topic -> {
                    var t = topicRepository.findById(topic.getId()).get();
                    return new TopicResponse(t, topic.getUser().getUsername(), topic.getUser().getPhoto());
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(finalList);
    }

    @GetMapping("/me")
    public ResponseEntity<List<Topic>> getMyTopics(Authentication authentication) {
        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        var result = topicRepository.findByUser(user);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Topic> getTopicById(@PathVariable("id") UUID id) {

        var topic = topicRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Topic does not exist"));
        return ResponseEntity.ok(topic);
    }

    @PostMapping("/add")
    public ResponseEntity<Topic> addTopic(@RequestBody RequestTopic requestTopic, Authentication authentication) {

        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        return ResponseEntity.ok(topicService.createTopic(user, requestTopic));
    }

    @GetMapping("/{title}/find")
    public ResponseEntity<List<TopicResponse>> findTopicsByTitle(@PathVariable("title") String title) {
        List<Topic> list = topicRepository.findByTitleContainingIgnoreCase(title);
        List<TopicResponse> result = list.stream()
                .map(topic -> new TopicResponse(topic, topic.getUser().getUsername(), topic.getUser().getPhoto()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }
}
