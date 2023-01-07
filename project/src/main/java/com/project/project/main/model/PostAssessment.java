package com.project.project.main.model;

import com.project.project.forum.model.Post;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;


@Entity
@Table(name = "post_assessment")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PostAssessment {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public static PostAssessment fromDto(User user, Post post) {
        return PostAssessment.builder()
                .id(UUID.randomUUID())
                .user(user)
                .post(post)
                .build();
    }
}
