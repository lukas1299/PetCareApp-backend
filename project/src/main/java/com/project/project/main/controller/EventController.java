package com.project.project.main.controller;

import com.project.project.main.model.Event;
import com.project.project.main.repository.EventRepository;
import com.project.project.main.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final EventRepository eventRepository;
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvent(){

        var events = eventRepository.findAll();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}/animals")
    public ResponseEntity<List<Event>> getEventsByAnimal(@PathVariable UUID id){
        var events = eventRepository.findByAnimalId(id);
        return ResponseEntity.ok(events);
    }

    @GetMapping("{id}/animals/event/year")
    public ResponseEntity<List<Event>> getEventsByYear(@PathVariable UUID id, @RequestParam int year){
        var events = eventService.getEventByYear(id, year);
        return ResponseEntity.ok(events);
    }

    @GetMapping("{id}/animals/event/month")
    public ResponseEntity<List<Event>> getEventsByMonth(@PathVariable UUID id, @RequestParam int month){
        var events = eventService.getEventByMonth(id, month);
        return ResponseEntity.ok(events);
    }
}
