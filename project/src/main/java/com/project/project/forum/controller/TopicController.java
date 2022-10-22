package com.project.project.forum.controller;

import com.project.project.forum.model.Topic;
import com.project.project.forum.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topics")
@RequiredArgsConstructor
public class TopicController {

    private final TopicRepository topicRepository;

    @GetMapping
    public ResponseEntity<List<Topic>> getAllTopics(){
        return ResponseEntity.ok(topicRepository.findAll());
    }

}
