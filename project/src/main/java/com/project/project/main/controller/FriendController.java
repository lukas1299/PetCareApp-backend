package com.project.project.main.controller;

import com.project.project.main.model.Friend;
import com.project.project.main.model.FriendStatus;
import com.project.project.main.model.User;
import com.project.project.main.repository.FriendRepository;
import com.project.project.main.repository.ProfileRepository;
import com.project.project.main.repository.UserRepository;
import com.project.project.main.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendController {

    private final ProfileRepository profileRepository;
    private final FriendRepository friendRepository;
    private final FriendService friendService;
    private final UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<List<User>> getUserFriends(Authentication authentication) {

        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));

        return ResponseEntity.ok(friendService.getFriend(user));
    }

    @GetMapping("/me/waiting")
    public ResponseEntity<List<User>> getMyWaitingInvitations(Authentication authentication){

        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));

        var userProfile = profileRepository.findByUserId(user.getId()).get();

        return ResponseEntity.ok(friendService.getMyWaitingInvitations(userProfile));
    }

    @GetMapping("/me/invitations")
    public ResponseEntity<List<Friend>> getInvitations(Authentication authentication){

        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));

        return ResponseEntity.ok(friendService.getInvitations(user));
    }

    @GetMapping("/{id}/user")
    public ResponseEntity<List<User>> getUserFriends(@PathVariable("id") UUID id){
        var user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));

        return ResponseEntity.ok(friendService.getFriend(user));
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<String> acceptInvitation(@PathVariable UUID id) throws Exception {

        var invitation = friendRepository.findById(id).orElseThrow(() -> new Exception("Invitation does not exists"));

        if(!invitation.getFriendStatus().equals(FriendStatus.WAITING)){
            throw new Exception("Unable to accept");
        }

        invitation.setFriendStatus(FriendStatus.ACCEPTED);
        friendRepository.save(invitation);

        return ResponseEntity.ok("Accepted");
    }

    @DeleteMapping("/{id}/removeFriend")
    public ResponseEntity<String> removeFriend(@PathVariable("id") UUID id) throws Exception {
        var user = userRepository.findById(id).orElseThrow(() -> new Exception("User does not exists"));
        var list =  friendService.removeFriend(user);
        friendRepository.deleteAll(list);
        return ResponseEntity.ok("Removed");
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<String> rejectFriend(@PathVariable UUID id) throws Exception {

        var invitation = friendRepository.findById(id).orElseThrow(() -> new Exception("Invitation does not exists"));

        friendRepository.delete(invitation);

        return ResponseEntity.ok("Rejected");
    }
}
