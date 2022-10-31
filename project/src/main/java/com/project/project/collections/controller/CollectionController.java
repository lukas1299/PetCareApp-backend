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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public Optional<List<Collection>> getMyCollections(Authentication authentication) throws Exception {

        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));

        var profile = profileRepository.findByUserId(user.getId()).orElseThrow(() -> new Exception("Profile does not exist"));

        return collectionRepository.findByProfile_id(profile.getId());
    }

    @GetMapping("/{id}/donations")
    public Optional<List<CollectionHistory>> getCollectionDonates(@PathVariable UUID id) throws Exception {

        var collection = collectionRepository.findById(id).orElseThrow(() -> new Exception("Collection does not exist"));

        return collectionHistoryRepository.findByCollection_id(collection.getId());

    }


    @PostMapping("/{id}/user/add")
    public ResponseEntity<Collection> addCollection(@RequestBody CollectionRequest collectionRequest, Authentication authentication) throws Exception {

        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        var profile = profileRepository.findByUserId(user.getId()).orElseThrow(() -> new Exception("User does not exist"));

        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        return ResponseEntity.ok(collectionRepository.save(Collection.fromDto(profile, collectionRequest, localDateTime.format(format))));

    }
    @PostMapping("/{id}/donate")
    public void donateCollection (@PathVariable UUID id, @RequestBody DonateRequest donateRequest, Authentication authentication) throws Exception {

        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        var collection = collectionRepository.findById(id).orElseThrow(() -> new Exception("Collection does not exist"));

        collectionHistoryRepository.save(CollectionHistory.fromDto(collection, user, donateRequest));
    }
}
