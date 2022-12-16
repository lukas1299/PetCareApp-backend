package com.project.project.forum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.project.main.model.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "topics")
@RequiredArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Topic implements Comparable<Topic> {
    @Id
    private UUID id;

    private String title;

    @Column(name = "topicCategory")
    @Enumerated(EnumType.STRING)
    private TopicCategory topicCategory;
    private String description;
    private String creationDate;

    @OneToMany(mappedBy = "topic", fetch = FetchType.LAZY)
    private List<Post> posts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Override
    public int compareTo(Topic o) {
        return getCreationDate().compareTo(o.getCreationDate());
    }

    public void addPost(Post post) {
        if (posts == null) {
            posts = new ArrayList<>();
        }
        posts.add(post);
    }

    public static Topic fromDto(RequestTopic requestTopic, String creationDate) {
        return Topic.builder()
                .id(UUID.randomUUID())
                .title(requestTopic.title())
                .topicCategory(requestTopic.topicCategory())
                .description(requestTopic.description())
                .creationDate(creationDate)
                .build();
    }
}
