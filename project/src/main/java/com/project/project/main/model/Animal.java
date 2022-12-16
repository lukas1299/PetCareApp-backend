package com.project.project.main.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Table(name = "animals")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Animal {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    private String name;
    private AnimalType type;
    private int age;
    private Double weight;

    @Column(name = "gender")
    private AnimalGender animalGender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "animal", fetch = FetchType.LAZY)
    private List<Event> events;


    public static Animal fromDto(AnimalRequest animalRequest){
        return Animal.builder()
                .id(UUID.randomUUID())
                .name(animalRequest.name())
                .type(animalRequest.animalType())
                .age(animalRequest.age())
                .weight(animalRequest.weight())
                .animalGender(animalRequest.gender())
                .build();
    }

}
