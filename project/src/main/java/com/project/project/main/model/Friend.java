package com.project.project.main.model;

import lombok.*;
import javax.persistence.*;
import java.util.UUID;

@Table(name = "friends")
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Friend {

    @Id
    @Column(name = "id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @Column(name = "status")
    private FriendStatus friendStatus;

    public static Friend fromDto(User user, Profile profile){
        return Friend.builder()
                .id(UUID.randomUUID())
                .user(user)
                .profile(profile)
                .friendStatus(FriendStatus.WAITING)
                .build();
    }

}
