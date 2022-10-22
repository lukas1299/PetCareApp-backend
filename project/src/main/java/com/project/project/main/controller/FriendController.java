package com.project.project.main.controller;

import com.project.project.main.model.FriendStatus;
import com.project.project.main.model.User;
import com.project.project.main.repository.FriendRepository;
import com.project.project.main.repository.ProfileRepository;
import com.project.project.main.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendController {

    private final ProfileRepository profileRepository;
    private final FriendRepository friendRepository;
    private final FriendService friendService;

    @GetMapping("/me")
    public ResponseEntity<List<User>> getUserFriends() {

        var profile = profileRepository.findByUserId(UUID.fromString("85564ae7-9796-434f-9b13-689ac3da5a1e")).orElseThrow(() -> new EntityNotFoundException("Profile does not exists"));

        return ResponseEntity.ok(friendService.getFriend(profile));
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<List<User>> acceptInvitation(@PathVariable UUID id) throws Exception {

        var invitation = friendRepository.findById(id).orElseThrow(() -> new Exception("Invitation does not exists"));

        if(!invitation.getFriendStatus().equals(FriendStatus.WAITING)){
            throw new Exception("Unable to accept");
        }

        invitation.setFriendStatus(FriendStatus.ACCEPTED);
        friendRepository.save(invitation);

        return ResponseEntity.ok(friendService.getFriend(invitation.getProfile()));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<List<User>> rejectFriend(@PathVariable UUID id) throws Exception {

        var invitation = friendRepository.findById(id).orElseThrow(() -> new Exception("Invitation does not exists"));

        invitation.setFriendStatus(FriendStatus.CANCELED);
        friendRepository.save(invitation);

        return ResponseEntity.ok(friendService.getFriend(invitation.getProfile()));
    }

}
