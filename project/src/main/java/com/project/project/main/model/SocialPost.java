package com.project.project.main.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "social_posts")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class SocialPost {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "date")
    private Date date;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    public static SocialPost fromDto(Profile profile, SocialPostRequest socialPostRequest){
        return SocialPost.builder()
                .id(UUID.randomUUID())
                .title(socialPostRequest.title())
                .content(socialPostRequest.content())
                .date(Date.from(Instant.now()))
                .profile(profile)
                .build();
    }

}
