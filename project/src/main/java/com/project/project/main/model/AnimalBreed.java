package com.project.project.main.model;

import lombok.*;

import javax.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "animal_breed")
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AnimalBreed {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    private String name;

    @OneToMany(mappedBy = "animalBreed")
    private List<AnimalFact> animalFactList;

    public static AnimalBreed fromDto(String name){
        return AnimalBreed.builder()
                .id(UUID.randomUUID())
                .name(name)
                .build();
    }
}
