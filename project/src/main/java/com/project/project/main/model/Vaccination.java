package com.project.project.main.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "vaccinations")
@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Vaccination {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    private String name;

    private VaccinationInterval interval;

    public static Vaccination fromDto(String content, VaccinationInterval vaccinationInterval){
        return Vaccination.builder()
                .id(UUID.randomUUID())
                .name(content)
                .interval(vaccinationInterval)
                .build();
    }
}

