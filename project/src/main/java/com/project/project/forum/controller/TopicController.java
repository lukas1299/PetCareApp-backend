package com.project.project.forum.controller;

import com.project.project.forum.model.RequestTopic;
import com.project.project.forum.model.Topic;
import com.project.project.forum.repository.TopicRepository;
import com.project.project.forum.service.TopicService;
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
@RequestMapping("/topics")
@RequiredArgsConstructor
public class TopicController {

    private final TopicRepository topicRepository;
    private final TopicService topicService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<Topic>> getAllTopics(){
        return ResponseEntity.ok(topicRepository.findAll());
    }

    @PostMapping("/add")
    public ResponseEntity<Topic> createTopic(@RequestBody RequestTopic requestTopic){

        //TODO get logged user
        var user = userRepository.findById(UUID.fromString("ecc5f51f-788b-47e2-99c8-9710e78323cb")).orElseThrow(() -> new EntityNotFoundException("User does not exists"));
        var topic = topicService.createTopic(user, requestTopic);

        return new ResponseEntity<>(topicRepository.save(topic), HttpStatus.CREATED);
    }

}
