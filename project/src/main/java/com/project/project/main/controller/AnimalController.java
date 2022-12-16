package com.project.project.main.controller;

import com.project.project.main.exception.EntityAlreadyExistsException;
import com.project.project.main.model.*;
import com.project.project.main.repository.AnimalRepository;
import com.project.project.main.repository.EventRepository;
import com.project.project.main.repository.UserRepository;
import com.project.project.main.service.AnimalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/animals")
public class AnimalController {

    private final AnimalRepository animalRepository;
    private final UserRepository userRepository;
    private final AnimalService animalService;
    private final EventRepository eventRepository;

    @GetMapping
    public ResponseEntity<List<Animal>> getAnimals() {
        return ResponseEntity.ok(animalRepository.findAll());
    }

    @GetMapping("/me")
    public ResponseEntity<List<Animal>> getUserAnimals(Authentication authentication) {

        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        return ResponseEntity.ok(user.getAnimals());
    }

    @PostMapping("/add")
    public ResponseEntity<Animal> createAnimal(@RequestBody AnimalRequest requestAnimal, Authentication authentication) {

        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        var animal = animalService.createAnimal(requestAnimal, user);
        return new ResponseEntity<>(animal, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/events/add")
    public ResponseEntity<List<Event>> addEvent(@PathVariable UUID id, @RequestBody EventRequest eventRequest) throws Exception {

        var animal = animalRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Animal does not exists"));
        var tempEvent = eventRepository.findByNameAndAnimalId(eventRequest.name(), id);
        if(!tempEvent.isEmpty()){
            throw new EntityAlreadyExistsException("Event already exists");
        }
        var event = animalService.addEventToAnimal(animal, eventRequest);
        return new ResponseEntity<>(event, HttpStatus.CREATED);
    }

    @DeleteMapping("/events/delete")
    public ResponseEntity<List<Event>> deleteEvent(@RequestBody EventDeleteRequest eventRequest) {

        return ResponseEntity.ok(animalService.removeEvents(eventRequest));

    }

}
