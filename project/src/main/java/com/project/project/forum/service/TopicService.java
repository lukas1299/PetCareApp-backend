package com.project.project.forum.service;

import com.project.project.forum.model.RequestTopic;
import com.project.project.forum.model.Topic;
import com.project.project.forum.repository.TopicRepository;
import com.project.project.main.model.User;
import com.project.project.main.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;

    public Topic createTopic(User user, RequestTopic requestTopic){

        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        var topic = Topic.fromDto(requestTopic, localDateTime.format(format));

        var topicList = user.getTopics();
        topic.setUser(user);

        topicRepository.save(topic);

        if(topicList.size() == 0){
            var newTopicsList = new ArrayList<Topic>();
            newTopicsList.add(topic);
            user.setTopics(newTopicsList);
        }

        return topic;
    }
}
