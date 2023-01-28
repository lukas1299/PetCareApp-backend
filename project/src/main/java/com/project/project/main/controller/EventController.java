package com.project.project.main.controller;

import com.project.project.main.model.*;
import com.project.project.main.repository.EventRepository;
import com.project.project.main.repository.UserRepository;
import com.project.project.main.service.EventService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/animals/{id}/all")
    public ResponseEntity<List<EventResponse>> getEventsByAnimal(@PathVariable UUID id) {
        var events = eventRepository.findByAnimalId(id).stream().sorted(Comparator.comparing(Event::getDate)).toList();
        var list = new ArrayList<EventResponse>();

        for(Event event : events){
            list.add(new EventResponse(event.getDate().toString(), event, null));
        }

        return ResponseEntity.ok(list);
    }

    @GetMapping("/animals/all")
    public ResponseEntity<List<EventResponse>> getAllAnimalsEvents(Authentication authentication){

        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        var animals = user.getAnimals();

        var resultList = animals.stream()
                .flatMap(animal -> eventRepository.findByAnimalId(animal.getId()).stream())
                .map(event -> new EventResponse(event.getDate().toString(), event, event.getAnimal()))
                .sorted(Comparator.comparing(EventResponse::date))
                .collect(Collectors.toList());

        return ResponseEntity.ok(resultList);
    }

    @GetMapping("/{id}/animals/event/year")
    public ResponseEntity<List<Event>> getEventsByYear(@PathVariable UUID id, @RequestParam int year) {
        var events = eventService.getEventByYear(id, year);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}/animals/event/month")
    public ResponseEntity<List<Event>> getEventsByMonth(@PathVariable UUID id, @RequestParam int month) {
        var events = eventService.getEventByMonth(id, month);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}/animals/deprecated")
    public ResponseEntity<List<Event>> getDeprecatedEvents(@PathVariable("id") UUID id){
        var list = eventService.getDeprecatedEvents(id);
        return ResponseEntity.ok(list);
    }
}