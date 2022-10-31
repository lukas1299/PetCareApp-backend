package com.project.project.main.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "roles")
@Setter
@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "role", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<UserRole> userRoles;

    public static Role fromDto(RoleRequest roleRequest){
        return Role.builder()
                .id(UUID.randomUUID())
                .name(roleRequest.name().toString())
                .build();
    }
}
