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
    public ResponseEntity<List<Animal>> getAnimals(){
        return ResponseEntity.ok(animalRepository.findAll());
    }

    @PostMapping("/add")
    public ResponseEntity<Animal> createAnimal(@RequestBody AnimalRequest requestAnimal){

        //TODO change to logged user
        var user = userRepository.findById(UUID.fromString("4faae376-fbd2-4f25-9af5-4b89924eadd6")).orElseThrow(() -> new EntityNotFoundException("User does not exists"));
        var animal = animalService.createAnimal(requestAnimal, user);

        return new ResponseEntity<>(animalRepository.save(animal), HttpStatus.CREATED);
    }

    @PostMapping("/events/add/{id}")
    public ResponseEntity<Event> addEvent(@PathVariable Long id, @RequestBody EventRequest eventRequest){

            var animal = animalRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Animal does not exists"));
            var event = animalService.addEventToAnimal(animal, eventRequest);

            return ResponseEntity.ok(event);
    }

}
