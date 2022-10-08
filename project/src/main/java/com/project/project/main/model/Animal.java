package com.project.project.main.model;

import lombok.*;

import javax.persistence.*;

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

}
