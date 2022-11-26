package com.project.project.main.model;

import com.project.project.achievements.model.ProfileAchievement;
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
    private User user;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY)
    private List<Friend> friends;

    @OneToMany(mappedBy = "profile")
    private List<ProfileAchievement> profileAchievements;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY)
    private List<SocialPost> socialPosts;

    public static Profile fromDto(User user){
        return Profile.builder()
                .id(UUID.randomUUID())
                .user(user)
                .build();
    }
}
