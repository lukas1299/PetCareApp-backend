package com.project.project.forum.service;

import com.project.project.forum.model.Post;
import com.project.project.forum.model.RequestPost;
import com.project.project.forum.model.Topic;
import com.project.project.forum.repository.PostRepository;
import com.project.project.forum.repository.TopicRepository;
import com.project.project.main.model.User;
import com.project.project.main.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final TopicRepository topicRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public List<Post> getPostsByTopic(UUID id){

        var topic = topicRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Topic does not exists"));

        List<Post> list = topic.getPosts();

        if(list.isEmpty()){
            return new ArrayList<>();
        }
        return list;
    }

    public Topic createPost(UUID topicId, RequestPost requestPost){
        var topic = topicRepository.findById(topicId).orElseThrow(() -> new EntityNotFoundException("Topic does not exist"));
        //TODO test
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        var post = Post.fromDto(requestPost,localDateTime.format(format));
        post.setTopic(topic);

        postRepository.save(post);

        var postList = topic.getPosts();

        if(postList.isEmpty()){
            var newPostsList = new ArrayList<Post>();
            newPostsList.add(post);
            topic.setPosts(newPostsList);
        }else {
            postList.add(post);
            topic.setPosts(postList);
        }

        return topicRepository.save(topic);
    }
    public Post realizeLikePost(Post post, User user) {

        var userFavouritePosts = user.getAssessedPosts();
        userFavouritePosts.add(post);
        user.setAssessedPosts(userFavouritePosts);
        post.setPositiveOpinionAmount(post.getPositiveOpinionAmount() + 1);

        post.setUser(user);

        userRepository.save(user);
        postRepository.save(post);
        return post;

    }
    public Post realizeDislikePost(Post post, User user){
        var userUnfavouredPosts = user.getAssessedPosts();
        userUnfavouredPosts.add(post);
        user.setAssessedPosts(userUnfavouredPosts);
        post.setNegativeOpinionAmount(post.getNegativeOpinionAmount() + 1);

        post.setUser(user);

        userRepository.save(user);
        postRepository.save(post);
        return post;
    }

}
