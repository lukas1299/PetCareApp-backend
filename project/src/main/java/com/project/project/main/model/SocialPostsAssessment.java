package com.project.project.main.model;

import lombok.*;
import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "social_posts_assessment")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class SocialPostsAssessment {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @ManyToOne
    @JoinColumn(name = "social_post_id")
    private SocialPost socialPost;

    public static SocialPostsAssessment fromDto(Profile profile, SocialPost socialPost) {
        return SocialPostsAssessment.builder()
                .id(UUID.randomUUID())
                .profile(profile)
                .socialPost(socialPost)
                .build();
    }
}
