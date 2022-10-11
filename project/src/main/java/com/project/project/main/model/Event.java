package com.project.project.main.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;
    private Date date;
    private EventType eventType;

    @ManyToOne
    @JoinColumn(name = "animal_id")
    @JsonIgnore
    private Animal animal;

    public static Event fromDto(EventRequest eventRequest){
        return Event.builder()
                .name(eventRequest.name())
                .date(eventRequest.data())
                .eventType(eventRequest.eventType())
                .build();
    }
}
