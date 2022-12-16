package com.project.project.main.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "social_posts")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class SocialPost implements Comparable<SocialPost> {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "date")
    private Date date;

    @Column(name = "positive_opinion_amount")
    private int positiveOpinionAmount;

    @Column(name = "negative_opinion_amount")
    private int negativeOpinionAmount;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @OneToMany(mappedBy = "socialPost", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<SocialPostsAssessment> socialPostsAssessments;

    @OneToMany(mappedBy = "socialPost")
    private List<PostComment> postCommentList;

    public static SocialPost fromDto(Profile profile, SocialPostRequest socialPostRequest){
        return SocialPost.builder()
                .id(UUID.randomUUID())
                .title(socialPostRequest.title())
                .content(socialPostRequest.content())
                .date(Date.from(Instant.now()))
                .positiveOpinionAmount(0)
                .negativeOpinionAmount(0)
                .profile(profile)
                .build();
    }

    @Override
    public int compareTo(SocialPost o) {
        return getDate().compareTo(o.getDate());
    }
}
