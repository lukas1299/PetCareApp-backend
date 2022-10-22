package com.project.project.main.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
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

    public static Profile fromDto(User user){
        return Profile.builder()
                .id(UUID.randomUUID())
                .user(user)
                .build();
    }

}
