package com.project.project.main.controller;

import com.project.project.main.model.Role;
import com.project.project.main.model.RoleRequest;
import com.project.project.main.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleRepository roleRepository;

    @PostMapping("/add")
    public ResponseEntity<Role> addRole(@RequestBody RoleRequest roleRequest){

        return ResponseEntity.ok(roleRepository.save(Role.fromDto(roleRequest)));
    }
}
