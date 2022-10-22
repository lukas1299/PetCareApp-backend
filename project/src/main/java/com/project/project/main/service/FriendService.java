package com.project.project.main.service;

import com.project.project.main.model.Friend;
import com.project.project.main.model.FriendStatus;
import com.project.project.main.model.Profile;
import com.project.project.main.model.User;
import com.project.project.main.repository.FriendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;

    public List<User> getFriend(Profile profile){

        List<User> friends = new ArrayList<>();

        for (Friend u : friendRepository.findAll()){
            if((profile.getId() == u.getProfile().getId()) && !u.getFriendStatus().equals(FriendStatus.CANCELED)){
                friends.add(u.getUser());
            }
        }
        return friends;
    }
}
