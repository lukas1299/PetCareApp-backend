package com.project.project.main.service;

import com.project.project.main.model.FriendRequest;
import com.project.project.main.model.Profile;
import com.project.project.main.repository.ProfileRepository;
import com.project.project.main.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    public Profile addUserToFriends(UUID id, FriendRequest friendRequest) {

        var user = userRepository.findByUsernameOrEmail(friendRequest.name(), null).orElseThrow(() -> new EntityNotFoundException("User does not exist"));
        var loggedUser = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User does not exist"));

        var profiles = loggedUser.getProfile();

        profiles.getFriends().add(user);
        user.getProfiles().add(profiles);

        var friends = profiles.getFriends();
        profiles.setFriends(friends);

        userRepository.save(user);

        return profileRepository.save(profiles);
    }
}
