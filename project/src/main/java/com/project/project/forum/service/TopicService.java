package com.project.project.forum.service;

import com.project.project.forum.model.RequestTopic;
import com.project.project.forum.model.Topic;
import com.project.project.main.model.User;
import com.project.project.main.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final UserRepository userRepository;

    public Topic createTopic(User user, RequestTopic requestTopic){

        var topic = new Topic();
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        topic.setCreationDate(localDateTime.format(format));
        topic.setTitle(requestTopic.message());

        var topicList = user.getTopics();
        topic.setUser(user);

        if(topicList.size() == 0){
            var newTopicsList = new ArrayList<Topic>();
            newTopicsList.add(topic);
            user.setTopics(newTopicsList);
        }
        userRepository.save(user);

        return topic;
    }
}
