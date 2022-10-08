package com.project.project.main.model;

//import com.project.project.registration.UserRequest;
import com.project.project.forum.model.Post;
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
    @Column(name = "user_id")
    private UUID id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Post> assessedPosts;


//    public static User fromDto(UserRequest userRequest){
//        return User.builder()
//                .email(userRequest.email().toLowerCase())
//                .username(userRequest.username().toLowerCase())
//                .password(userRequest.password())
//                .build();
//    }

}
