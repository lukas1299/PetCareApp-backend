package com.project.project.main.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Type;

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
    private int age;
    private Double weight;

    @Type(type="org.hibernate.type.BinaryType")
    private byte[] photo;

    @Column(name = "gender")
    private AnimalGender animalGender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "animal", fetch = FetchType.LAZY)
    private List<Event> events;

    @ManyToOne
    @JoinColumn(name = "animal_breed_id")
    private AnimalBreed animalBreed;

    public static Animal fromDto(AnimalRequest animalRequest, byte[] bytes, AnimalBreed animalBreed){
        return Animal.builder()
                .id(UUID.randomUUID())
                .name(animalRequest.name())
                .animalBreed(animalBreed)
                .age(animalRequest.age())
                .weight(animalRequest.weight())
                .photo(bytes)
                .animalGender(animalRequest.gender())
                .build();
    }
}
