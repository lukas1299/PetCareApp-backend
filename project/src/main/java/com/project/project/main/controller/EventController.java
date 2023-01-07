package com.project.project.main.controller;

import com.project.project.main.model.*;
import com.project.project.main.repository.EventRepository;
import com.project.project.main.repository.UserRepository;
import com.project.project.main.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
@RequestMapping("/events")
public class EventController {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvent() {

        var events = eventRepository.findAll();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}/all")
    public ResponseEntity<List<Day>> getFullEventsCalendar(@PathVariable UUID id) {
        var events = eventService.getFullEventCalendar(id);
        return ResponseEntity.ok(events);
    }

    @PostMapping("/day/{id}/all")
    public ResponseEntity<List<Event>> getFullEventsCalendarByDay(@PathVariable UUID id, @RequestBody EventDateRequest eventDateRequest) throws ParseException {
        var events = eventService.getFullEventCalendarByDay(id, eventDateRequest);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}/animals")
    public ResponseEntity<List<Event>> getEventsByAnimal(@PathVariable UUID id) {
        var events = eventRepository.findByAnimalId(id);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/animals/all")
    public ResponseEntity<List<EventResponse>> getAllAnimalsEvents(Authentication authentication){

        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        var animals = user.getAnimals();

        var resultList = animals.stream()
                .flatMap(animal -> eventRepository.findByAnimalId(animal.getId()).stream())
                .map(event -> new EventResponse(event.getDate().toString(), event))
                .sorted(Comparator.comparing(EventResponse::date))
                .collect(Collectors.toList());

        return ResponseEntity.ok(resultList);
    }

    @GetMapping("{id}/animals/event/year")
    public ResponseEntity<List<Event>> getEventsByYear(@PathVariable UUID id, @RequestParam int year) {
        var events = eventService.getEventByYear(id, year);
        return ResponseEntity.ok(events);
    }

    @GetMapping("{id}/animals/event/month")
    public ResponseEntity<List<Event>> getEventsByMonth(@PathVariable UUID id, @RequestParam int month) {
        var events = eventService.getEventByMonth(id, month);
        return ResponseEntity.ok(events);
    }
}
