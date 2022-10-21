package com.project.project.main.model;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "friends")
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Friend {

    @Id
    private UUID id;

    private UUID profile_id;
    private UUID user_id;

}
