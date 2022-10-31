package com.project.project.main.model;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "role_user")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserRole {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

}
