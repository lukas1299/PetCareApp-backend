package com.project.project.main.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "events")
@RequiredArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Event {
    @Id
    @Column(name = "id")
    private UUID id;

    private String name;
    private String date;
    private EventType eventType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id")
    @JsonIgnore
    private Animal animal;

    public static Event fromDto(EventRequest eventRequest){
        return Event.builder()
                .id(UUID.randomUUID())
                .name(eventRequest.name())
                .eventType(eventRequest.eventType())
                .build();
    }
}
