package com.project.project.collections.controller;

import com.project.project.collections.model.Collection;
import com.project.project.collections.model.CollectionHistory;
import com.project.project.collections.model.CollectionRequest;
import com.project.project.collections.model.DonateRequest;
import com.project.project.collections.repository.CollectionHistoryRepository;
import com.project.project.collections.repository.CollectionRepository;
import com.project.project.main.repository.ProfileRepository;
import com.project.project.main.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/collections")
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionRepository collectionRepository;
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final CollectionHistoryRepository collectionHistoryRepository;

    @GetMapping("/me")
    public Optional<List<Collection>> getMyCollections() throws Exception {

        var profile = profileRepository.findByUserId(UUID.fromString("7ff63066-82cb-4980-a318-0e07447b4744")).orElseThrow(() -> new Exception("User does not exist"));

        return collectionRepository.findByProfile_id(profile.getId());
    }

    @GetMapping("/{id}/donations")
    public Optional<List<CollectionHistory>> getCollectionDonates(@PathVariable UUID id) throws Exception {

        var collection = collectionRepository.findById(id).orElseThrow(() -> new Exception("Collection does not exist"));

        return collectionHistoryRepository.findByCollection_id(collection.getId());

    }


    @PostMapping("/{id}/user/add")
    public ResponseEntity<Collection> addCollection(@PathVariable UUID id, @RequestBody CollectionRequest collectionRequest) throws Exception {

        //TODO Change id to authenticated user id
        var profile = profileRepository.findByUserId(id).orElseThrow(() -> new Exception("User does not exist"));

        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        return ResponseEntity.ok(collectionRepository.save(Collection.fromDto(profile, collectionRequest, localDateTime.format(format))));

    }
    @PostMapping("/{id}/donate")
    public void donateCollection (@PathVariable UUID id, @RequestBody DonateRequest donateRequest) throws Exception {

        //TODO Change id to authenticated user id
        var user = userRepository.findAll().get(0);

        var collection = collectionRepository.findById(id).orElseThrow(() -> new Exception("Collection does not exist"));

        collectionHistoryRepository.save(CollectionHistory.fromDto(collection, user, donateRequest));
    }
}
