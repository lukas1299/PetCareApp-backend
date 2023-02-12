package com.project.project.main.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "profiles")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Profile {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY)
    private List<Friend> friends;

    @JsonIgnore
    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY)
    private List<SocialPost> socialPosts;

    @OneToMany(mappedBy = "profile")
    private List<CompetitionDetailsAssessment> competitionDetailsAssessment;

    public static Profile fromDto(User user){
        return Profile.builder()
                .id(UUID.randomUUID())
                .user(user)
                .build();
    }
}
