package com.project.project.forum.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "topics")
@RequiredArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topic_id", nullable = false)
    private Long id;

    private String title;
    private String creationDate;

    @OneToMany(mappedBy = "topic")
    private List<Post> posts;

}
