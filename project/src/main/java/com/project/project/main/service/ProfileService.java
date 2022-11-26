package com.project.project.main.service;

import com.project.project.main.model.Friend;
import com.project.project.main.model.FriendRequest;
import com.project.project.main.model.Profile;
import com.project.project.main.repository.FriendRepository;
import com.project.project.main.repository.ProfileRepository;
import com.project.project.main.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final FriendRepository friendRepository;

    public Profile createProfile(UUID id) throws Exception {

        var user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User does not exist"));

        if (profileRepository.findByUserId(id).isPresent()) {
            throw new Exception("Entity currently exists");
        }

        var profile = Profile.fromDto(user);

        profile.setUser(user);
        profileRepository.save(profile);
        user.setProfile(profile);
        userRepository.save(user);

        return profile;

    }

    public void addUserToFriends(Authentication authentication, FriendRequest friendRequest) throws Exception {

        var user = userRepository.findByUsernameOrEmail(friendRequest.name(), null).orElseThrow(() -> new Exception("User does not exist"));

        var me = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new Exception("User does not exist"));

        var profile = profileRepository.findByUserId(me.getId()).orElseThrow(() -> new Exception("Profile does not exist"));

        if (friendRepository.findByProfileIdAndUserId(profile.getId(), user.getId()).isPresent()) {
            throw new Exception("Friend currently exist");
        }

        friendRepository.save(Friend.fromDto(user, profile));

    }
}
