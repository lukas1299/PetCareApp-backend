package com.project.project.security;

import com.project.project.main.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        userRepository.findByUsernameOrEmail(username, null).get().getUserRoles().forEach(userRole ->{
            authorities.add(new SimpleGrantedAuthority(userRole.getRole().getName()));
        });

        return userRepository.findByUsernameOrEmail(username, "s")
                .map(person -> new User(person.getUsername(), person.getPassword(), authorities))
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("Bad credentials"));
    }
}
