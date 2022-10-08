package com.project.project.forum.controller;

import com.project.project.forum.model.RequestTopic;
import com.project.project.forum.model.Topic;
import com.project.project.forum.repository.TopicRepository;
import com.project.project.forum.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topics")
@RequiredArgsConstructor
public class TopicController {

    private final TopicRepository topicRepository;
    private final TopicService topicService;

    @GetMapping
    public ResponseEntity<List<Topic>> getAllTopics(){
        return ResponseEntity.ok(topicRepository.findAll());
    }

    @PostMapping("/add")
    public ResponseEntity<Topic> createTopic(@RequestBody RequestTopic requestTopic){
        
        var topic = topicService.createTopic(requestTopic);

        return new ResponseEntity<>(topicRepository.save(topic), HttpStatus.CREATED);
    }

}
