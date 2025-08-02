package com.jobhunter.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import com.jobhunter.jobhunter.domain.User;
import com.jobhunter.jobhunter.domain.dto.response.user.ResCreateUserDTO;
import com.jobhunter.jobhunter.domain.dto.response.user.ResUpdateUserDTO;
import com.jobhunter.jobhunter.domain.dto.response.user.ResUserDTO;
import com.jobhunter.jobhunter.domain.dto.result.ResultPaginationDTO;
import com.jobhunter.jobhunter.service.UserService;
import com.jobhunter.jobhunter.util.annotation.ApiMessage;
import com.jobhunter.jobhunter.util.exception.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/users/{id}")
    @ApiMessage("Get a user")
    public ResponseEntity<ResUserDTO> getUser(@PathVariable("id") Long id) throws IdInvalidException {
        if (this.userService.fetchUserById(id).isPresent()) {
            throw new IdInvalidException("Id user: " + id + " not found");
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(this.userService.convertToResUserDTO(this.userService.fetchUserById(id).get()));
    }

    @GetMapping("/users")
    @ApiMessage("Fetch all users")
    public ResponseEntity<ResultPaginationDTO> getUsers(@Filter Specification<User> spec, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.fetchUsers(spec, pageable));
    }

    @PostMapping("/users")
    @ApiMessage("Create a user")
    public ResponseEntity<ResCreateUserDTO> createUser(@Valid @RequestBody User userJson) throws IdInvalidException {
        if (this.userService.checkExistedEmail(userJson.getEmail())) {
            throw new IdInvalidException(userJson.getEmail() + "was Existed email");
        }
        userJson.setPassword(passwordEncoder.encode(userJson.getPassword()));
        User user = this.userService.saveUser(userJson);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToResCreateUserDTO(user));
    }

    @DeleteMapping("/users/{id}")
    @ApiMessage("Delete a user")
    public ResponseEntity<Void> deleteUserById(@PathVariable("id") long id) throws IdInvalidException {
        if (this.userService.fetchUserById(id) == null) {
            throw new IdInvalidException("Id user: " + id + " not found");
        }

        this.userService.deleteUserById(id);
        return ResponseEntity.ok(null);
    }

    @PutMapping("/users")
    @ApiMessage("Update a user")
    public ResponseEntity<ResUpdateUserDTO> updateUser(@RequestBody User userJson) throws IdInvalidException {
        User user = this.userService.fetchUserById(userJson.getId()).get();

        if (user == null) {
            throw new IdInvalidException("Id user: " + userJson.getId() + " not found");
        }
        if (!userJson.getEmail().equals(userJson.getEmail())) {
            if (this.userService.checkExistedEmail(userJson.getEmail())) {
                throw new IdInvalidException(userJson.getEmail() + "was Existed email");
            }
        }
        user = this.userService.updateUser(userJson);
        return ResponseEntity.ok(this.userService.convertToResUpdateUserDTO(user));

    }
}
