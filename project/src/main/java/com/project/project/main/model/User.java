package com.project.project.main.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.project.collections.model.CollectionHistory;
import com.project.project.forum.model.Post;
import com.project.project.forum.model.Topic;
import lombok.*;
import org.hibernate.annotations.Type;

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

    @Type(type="org.hibernate.type.BinaryType")
    private byte[] photo;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Post> assessedPosts;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<PostAssessment> postAssessments;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Topic> topics;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Animal> animals;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Friend> friends;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CollectionHistory> collections;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    @JsonIgnore
    private Profile profile;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<QuizResult> quizResult;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<UserRole> userRoles;

    public static User fromDto(UserRequest userRequest, byte[] file) {
        return User.builder()
                .id(UUID.randomUUID())
                .email(userRequest.email().toLowerCase())
                .username(userRequest.username().toLowerCase())
                .password(userRequest.password())
                .photo(file)
                .build();
    }
}
