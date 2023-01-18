package com.project.project.main.service;

import com.project.project.main.model.Friend;
import com.project.project.main.model.FriendStatus;
import com.project.project.main.model.Profile;
import com.project.project.main.model.User;
import com.project.project.main.repository.FriendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;

    public List<User> getFriend(User user) {
        return friendRepository.findAll().stream()
                .filter(friend -> friend.getUser().getId().equals(user.getId()) || friend.getProfile().getUser().getId().equals(user.getId()))
                .filter(friend -> !friend.getFriendStatus().equals(FriendStatus.CANCELED) && !friend.getFriendStatus().equals(FriendStatus.WAITING))
                .map(friend -> friend.getUser().getId().equals(user.getId()) ? friend.getProfile().getUser() : friend.getUser())
                .distinct()
                .collect(Collectors.toList());
    }

    public List<User> getMyWaitingInvitations(Profile profile) {
        return friendRepository.findAll().stream()
                .filter(friend -> profile.getId() == friend.getProfile().getId() && friend.getFriendStatus().equals(FriendStatus.WAITING))
                .map(friend -> friend.getProfile().getUser())
                .collect(Collectors.toList());
    }

    public List<Friend> getInvitations(User user) {
        return friendRepository.findByUserId(user.getId()).stream()
                .filter(friend -> friend.getFriendStatus().equals(FriendStatus.WAITING))
                .collect(Collectors.toList());
    }

    public List<Friend> removeFriend(User user) {
        return friendRepository.findAll().stream()
                .filter(friend -> friend.getUser().getId().equals(user.getId()) || friend.getProfile().getUser().getId().equals(user.getId()))
                .filter(friend -> friend.getFriendStatus().equals(FriendStatus.ACCEPTED)).toList();
    }

}
