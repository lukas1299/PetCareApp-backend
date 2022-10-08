package com.project.project.forum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.project.main.model.User;
import lombok.*;
import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "posts")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String message;
    private int positiveOpinionAmount;
    private int negativeOpinionAmount;
    private String postCreationDate;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    @JsonIgnore
    private Topic topic;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

}
