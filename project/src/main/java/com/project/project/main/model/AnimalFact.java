package com.project.project.main.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "animal_facts")
@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class AnimalFact {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    private String name;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "animal_breed_id")
    private AnimalBreed animalBreed;

    public static AnimalFact fromDto(String name){
        return AnimalFact.builder()
                .id(UUID.randomUUID())
                .name(name)
                .build();
    }
}
