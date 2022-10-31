package com.project.project.main.controller;

import com.project.project.main.model.Animal;
import com.project.project.main.model.AnimalRequest;
import com.project.project.main.model.Event;
import com.project.project.main.model.EventRequest;
import com.project.project.main.repository.AnimalRepository;
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
@RequiredArgsConstructor
@RequestMapping("/animals")
public class AnimalController {

    private final AnimalRepository animalRepository;
    private final UserRepository userRepository;
    private final AnimalService animalService;

    @GetMapping
    public ResponseEntity<List<Animal>> getAnimals() {
        return ResponseEntity.ok(animalRepository.findAll());
    }

    @PostMapping("/add")
    public ResponseEntity<Animal> createAnimal(@RequestBody AnimalRequest requestAnimal, Authentication authentication) {

        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        var animal = animalService.createAnimal(requestAnimal, user);
        return new ResponseEntity<>(animal, HttpStatus.CREATED);
    }

    @PostMapping("/events/add")
    public ResponseEntity<Event> addEvent(@RequestBody EventRequest eventRequest) {

        var animal = animalRepository.findByName(eventRequest.animalName()).orElseThrow(() -> new EntityNotFoundException("Animal does not exists"));
        var event = animalService.addEventToAnimal(animal, eventRequest);
        return new ResponseEntity<>(event, HttpStatus.CREATED);
    }
}
