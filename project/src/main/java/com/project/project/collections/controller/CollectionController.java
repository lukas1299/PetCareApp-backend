package com.project.project.collections.controller;

import com.project.project.collections.model.*;
import com.project.project.collections.repository.CollectionHistoryRepository;
import com.project.project.collections.repository.CollectionRepository;
import com.project.project.collections.service.CollectionService;
import com.project.project.main.repository.ProfileRepository;
import com.project.project.main.repository.UserRepository;
import com.project.project.main.service.FriendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
@RequestMapping("/collections")
public class CollectionController {

    private final CollectionRepository collectionRepository;
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final CollectionHistoryRepository collectionHistoryRepository;
    private final FriendService friendService;
    private final CollectionService collectionService;

    @GetMapping("/me")
    public ResponseEntity<List<CollectionResponse>> getMyCollections(Authentication authentication) throws Exception {

        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        var profile = profileRepository.findByUserId(user.getId()).orElseThrow(() -> new Exception("Profile does not exist"));
        var result = collectionService.getCollectionInfo(profile);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/friends")
    public ResponseEntity<List<CollectionResponse>> getFriendsCollections(Authentication authentication) {
        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        var friends = friendService.getFriend(user);
        var result = new ArrayList<CollectionResponse>();

        friends.forEach(friend -> result.addAll(collectionService.getCollectionInfo(profileRepository.findByUserId(friend.getId()).get())));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/other")
    public ResponseEntity<List<CollectionResponse>> getOtherPeopleCollections(Authentication authentication) {
        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));

        var result = new ArrayList<CollectionResponse>();

        var friends = friendService.getFriend(user);
        friends.add(user);

        var users = userRepository.findAll();
        users.removeAll(friends);

        users.forEach(u -> result.addAll(collectionService.getCollectionInfo(profileRepository.findByUserId(u.getId()).get())));

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}/donations")
    public Optional<List<CollectionHistory>> getCollectionDonates(@PathVariable UUID id) throws Exception {

        var collection = collectionRepository.findById(id).orElseThrow(() -> new Exception("Collection does not exist"));
        return collectionHistoryRepository.findByCollection_id(collection.getId());
    }

    @PostMapping("/add")
    public ResponseEntity<Collection> addCollection(@RequestBody CollectionRequest collectionRequest, Authentication authentication) throws Exception {

        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        var profile = profileRepository.findByUserId(user.getId()).orElseThrow(() -> new Exception("User does not exist"));

        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        return ResponseEntity.ok(collectionRepository.save(Collection.fromDto(profile, collectionRequest, localDateTime.format(format))));

    }

    @PostMapping("/{id}/donate")
    public void donateCollection(@PathVariable UUID id, @RequestBody DonateRequest donateRequest, Authentication authentication) throws Exception {

        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        var collection = collectionRepository.findById(id).orElseThrow(() -> new Exception("Collection does not exist"));

        collectionHistoryRepository.save(CollectionHistory.fromDto(collection, user, donateRequest));
    }

    @GetMapping("/{title}/find")
    public ResponseEntity<?> findCollectionByTitle(@PathVariable("title") String title){

        var result = collectionService.getFoundCollection(title);

        return ResponseEntity.ok(result);
    }
}
