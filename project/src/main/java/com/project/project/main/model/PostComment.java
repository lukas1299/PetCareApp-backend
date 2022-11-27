package com.project.project.main.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Table(name = "post_comment")
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
    private Date date;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "social_posts_id")
    private SocialPost socialPost;

    public static PostComment fromDto(PostCommentRequest postCommentRequest){
        return PostComment.builder()
                .id(UUID.randomUUID())
                .content(postCommentRequest.content())
                .date(Date.from(Instant.now()))
                .build();
    }

}
