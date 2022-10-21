package com.project.project.forum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.project.main.model.User;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "topics")
@RequiredArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Topic {
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
