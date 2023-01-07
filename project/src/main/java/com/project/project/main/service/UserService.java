package com.project.project.main.service;

import com.project.project.main.model.*;
import com.project.project.main.repository.RoleRepository;
import com.project.project.main.repository.UserRepository;
import com.project.project.main.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ProfileService profileService;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final FriendService friendService;

    public User createUser(UserRequest userRequest, MultipartFile file) throws Exception {

        var user =  User.fromDto(userRequest, file == null ? null : file.getBytes());

        if(userRepository.findByUsernameOrEmail(userRequest.username(), userRequest.email()).isPresent()){
            throw new Exception("User currently exist");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        var role = roleRepository.findByName(userRequest.role().name()).orElseThrow(() -> new EntityNotFoundException("Role does not exist"));

        var newUser = userRepository.save(user);
        profileService.createProfile(newUser.getId());

        var userRole = userRoleRepository.save(new UserRole(UUID.randomUUID(), newUser, role));

        var userRoleList = newUser.getUserRoles();
        if(userRoleList == null){
            userRoleList = new ArrayList<>();
        }
        userRoleList.add(userRole);

        var roleUserList = role.getUserRoles();
        if(roleUserList == null){
            roleUserList = new ArrayList<>();
        }
        roleUserList.add(userRole);

        return newUser;
    }

    public List<User> getNonFriendsUsers(User user) {
        var userFriends = friendService.getFriend(user);

        return userRepository.findAll().stream()
                .filter(u -> !u.getId().equals(user.getId()))
                .filter(friend -> userFriends.stream().noneMatch(randomUser -> randomUser.getId().equals(friend.getId())))
                .toList();
    }
}
