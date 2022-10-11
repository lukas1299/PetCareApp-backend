package com.project.project.main.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private AnimalType type;
    private int age;
    private Double weight;
    @Column(name = "gender")
    private AnimalGender animalGender;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "animal")
    private List<Event> events;

    public static Animal fromDto(AnimalRequest animalRequest){
        return Animal.builder()
                .name(animalRequest.name())
                .type(animalRequest.animalType())
                .age(animalRequest.age())
                .weight(animalRequest.weight())
                .animalGender(animalRequest.gender())
                .build();
    }

}
