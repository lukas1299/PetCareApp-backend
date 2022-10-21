package com.project.project.main.model;

import com.project.project.forum.model.Post;
import com.project.project.forum.model.Topic;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class User {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Post> assessedPosts;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Topic> topics;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Animal> animals;


    public static User fromDto(UserRequest userRequest) {
        return User.builder()
                .id(UUID.randomUUID())
                .email(userRequest.email().toLowerCase())
                .username(userRequest.username().toLowerCase())
                .password(userRequest.password())
                .build();
    }
}
