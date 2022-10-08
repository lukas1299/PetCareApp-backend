package com.project.project.forum.service;

import com.project.project.forum.model.RequestTopic;
import com.project.project.forum.model.Topic;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class TopicService {

    public Topic createTopic(RequestTopic requestTopic){

        var topic = new Topic();
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        topic.setCreationDate(localDateTime.format(format));
        topic.setTitle(requestTopic.message());

        return topic;
    }
}
