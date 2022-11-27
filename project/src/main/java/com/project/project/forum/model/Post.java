package com.project.project.forum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.project.main.model.User;
import lombok.*;
import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "posts")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post implements Comparable<Post> {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "message")
    private String message;

    @Column(name = "positive_opinion_amount")
    private int positiveOpinionAmount;

    @Column(name = "negative_opinion_amount")
    private int negativeOpinionAmount;

    @Column(name = "post_creation_date")
    private String postCreationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    @JsonIgnore
    private Topic topic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public static Post fromDto(RequestPost requestPost, String date){
        return Post.builder()
                .id(UUID.randomUUID())
                .postCreationDate(date)
                .message(requestPost.message())
                .build();
    }

    @Override
    public int compareTo(Post o) {
        return getPostCreationDate().compareTo(o.getPostCreationDate());
    }
}
