package com.project.project.main.service;

import com.project.project.main.exception.AssessmentException;
import com.project.project.main.exception.ObjectNotFoundException;
import com.project.project.main.model.*;
import com.project.project.main.repository.ProfileRepository;
import com.project.project.main.repository.SocialPostRepository;
import com.project.project.main.repository.SocialPostsAssessmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SocialPostService {

    private final ProfileRepository profileRepository;
    private final SocialPostRepository socialPostRepository;
    private final FriendService friendService;
    private final SocialPostsAssessmentRepository socialPostsAssessmentRepository;

    public SocialPost createSocialPost(User user, SocialPostRequest socialPostRequest, MultipartFile file) throws IOException {

        var profile = profileRepository.findByUserId(user.getId()).orElseThrow(() -> new ObjectNotFoundException("Profile does not exist"));

        var socialPost = SocialPost.fromDto(profile, socialPostRequest, file == null ? null : file.getBytes());

        return socialPostRepository.save(socialPost);
    }

    public List<SocialPost> getFriendsPosts(Profile profile) {

        var list = friendService.getFriend(profile.getUser());
        list.add(profile.getUser());

        return list.stream()
                .map(friend -> profileRepository.findByUserId(friend.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Profile::getSocialPosts)
                .flatMap(List::stream)
                .sorted()
                .collect(Collectors.toList());
    }

    public SocialPost assessmentRealization(User user, SocialPost socialPost, String assessmentType) {
        var profile = profileRepository.findByUserId(user.getId()).orElseThrow(() -> new ObjectNotFoundException("Profile does not exist"));

        var result = socialPostsAssessmentRepository.findByProfileAndSocialPost(profile, socialPost);

        if (!result.isEmpty()) {
            throw new AssessmentException();
        }

        var socialPostResult = socialPostRepository.findById(socialPost.getId()).orElseThrow(() -> new EntityNotFoundException("Social post does not exist"));

        switch (assessmentType) {
            case "like" -> socialPostResult.setPositiveOpinionAmount(socialPostResult.getPositiveOpinionAmount() + 1);
            case "dislike" -> socialPostResult.setNegativeOpinionAmount(socialPostResult.getNegativeOpinionAmount() + 1);
        }

        socialPostsAssessmentRepository.save(SocialPostsAssessment.fromDto(profile, socialPostResult));

        return socialPostResult;
    }
}
