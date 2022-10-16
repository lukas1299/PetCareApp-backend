package com.project.project.main.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Profile {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @OneToOne(mappedBy = "profile")
    @JsonIgnore
    private User user;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "friends",
            joinColumns = {@JoinColumn(name = "profile_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private Set<User> friends = new HashSet<>();

}
