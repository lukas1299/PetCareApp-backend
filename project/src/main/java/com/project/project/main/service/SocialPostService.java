package com.project.project.main.service;

import com.project.project.main.exception.ObjectNotFoundException;
import com.project.project.main.model.*;
import com.project.project.main.repository.ProfileRepository;
import com.project.project.main.repository.SocialPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class SocialPostService {

    private final ProfileRepository profileRepository;
    private final SocialPostRepository socialPostRepository;

    public SocialPost createSocialPost(User user, SocialPostRequest socialPostRequest){

        var profile = profileRepository.findByUserId(user.getId()).orElseThrow(() -> new ObjectNotFoundException("Profile does not exist"));

        var socialPost = SocialPost.fromDto(profile, socialPostRequest);
        return socialPostRepository.save(socialPost);

    }

    public ArrayList<SocialPost> getFriendsPosts(Profile profile){
        var friendsPostList = new ArrayList<SocialPost>();

        for (Friend friend : profile.getFriends()) {
            var friendPosts = profileRepository.findByUserId(friend.getUser().getId()).orElseThrow(() -> new ObjectNotFoundException("Profile does not exist")).getSocialPosts();
            friendsPostList.addAll(friendPosts);
        }
        return friendsPostList;
    }
}
