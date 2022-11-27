package com.project.project.main.model;

import lombok.*;
import javax.persistence.*;
import java.util.UUID;

@Table(name = "postComment")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostComment {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "content")
    private String content;

    @Column(name = "date")
    private String date;

    @ManyToOne
    @JoinColumn(name = "social_post_id")
    private SocialPost socialPost;

    public PostComment fromDto(PostCommentRequest postCommentRequest){
        return PostComment.builder()
                .id(UUID.randomUUID())
                .content(postCommentRequest.content())
                .date(postCommentRequest.date())
                .build();
    }

}
