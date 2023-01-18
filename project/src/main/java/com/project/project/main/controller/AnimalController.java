package com.project.project.main.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.project.main.exception.EntityAlreadyExistsException;
import com.project.project.main.model.*;
import com.project.project.main.repository.AnimalRepository;
import com.project.project.main.repository.EventRepository;
import com.project.project.main.repository.UserRepository;
import com.project.project.main.service.AnimalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
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
    private final ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<List<Animal>> getAnimals() {
        return ResponseEntity.ok(animalRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Animal> getAnimalById(@PathVariable("id") UUID id) {
        var animal = animalRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Animal does not exists"));
        return ResponseEntity.ok(animal);
    }

    @GetMapping("/{id}/user")
    public ResponseEntity<List<Animal>> getUserAnimal(@PathVariable("id") UUID id) throws Exception {
        var user = userRepository.findById(id).orElseThrow(() -> new Exception("User does not exists"));
        var list = animalRepository.findByUser(user);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/me")
    public ResponseEntity<List<Animal>> getUserAnimals(Authentication authentication) {

        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        return ResponseEntity.ok(user.getAnimals());
    }

    @PostMapping("/add")
    public ResponseEntity<Animal> createAnimal(@RequestParam("file") MultipartFile file, @RequestParam("json") String json, Authentication authentication) throws IOException {

        var requestAnimal = objectMapper.readValue(json, AnimalRequest.class);
        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        var animal = animalService.createAnimal(requestAnimal, user, file);
        return new ResponseEntity<>(animal, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<Animal> updateAnimal(@PathVariable("id") UUID id, @RequestParam("file") MultipartFile file, @RequestParam("json") String json) throws IOException {

        var animal = animalRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Animal does not exists"));
        var requestAnimal = objectMapper.readValue(json, AnimalRequest.class);
        animal.setName(requestAnimal.name());
        animal.setWeight(requestAnimal.weight());
        animal.setAge(requestAnimal.age());
        animal.setType(requestAnimal.animalType());
        animal.setAnimalGender(requestAnimal.gender());
        animal.setPhoto(file.getBytes());
        animalRepository.save(animal);
        return new ResponseEntity<>(animal, HttpStatus.OK);
    }

    @PostMapping("/{id}/events/add")
    public ResponseEntity<List<Event>> addEvent(@PathVariable UUID id, @RequestBody EventRequest eventRequest) throws Exception {

        var animal = animalRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Animal does not exists"));
        var tempEvent = eventRepository.findByNameAndAnimalId(eventRequest.name(), id);
        if (!tempEvent.isEmpty()) {
            throw new EntityAlreadyExistsException("Event already exists");
        }
        var event = animalService.addEventToAnimal(animal, eventRequest);
        return new ResponseEntity<>(event, HttpStatus.CREATED);
    }

    @DeleteMapping("/events/delete")
    public ResponseEntity<List<Event>> deleteEvent(@RequestBody EventDeleteRequest eventRequest) {

        return ResponseEntity.ok(animalService.removeEvents(eventRequest));
    }

    @DeleteMapping("/{id}/delete")
    public void deleteAnimal(@PathVariable("id") UUID id) {
        var animal = animalRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Animal does not exists"));
        eventRepository.deleteAll(animal.getEvents());
        animalRepository.delete(animal);
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getAnimalImage(@PathVariable UUID id) {
        var animal = animalRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Animal does not exists"));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + animal.getName() + "\"")
                .contentType(MediaType.IMAGE_JPEG)
                .body(animal.getPhoto());
    }

    @PostMapping("/{name}/find")
    public ResponseEntity<?> findAnimal(@PathVariable(value = "name") String name) {
        var list = animalRepository.findAll().stream()
                .filter(animal -> animal.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
        return ResponseEntity.ok(list);
    }
}
