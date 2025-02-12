package vn.hoidanit.jobhunter.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable("id") Long id) {
        if (id == null) {
            return null;
        }
        return this.userService.fetchUserById(id);
    }

    @GetMapping("/user")
    public List<User> getAllUsers() {
        return this.userService.fetchAllUsers();
    }

    @PostMapping("/user")
    public User createUser(@RequestBody User userJson) {
        // User user = new User();
        // user.setEmail("vthanhtrongng@gmail.com");
        // user.setName("Thanh Trong");
        // user.setPassword("Thanhtrong@0510");

        User user = this.userService.saveUser(userJson);
        return user;
    }

    @DeleteMapping("/user/{id}")
    public String deleteUserById(@PathVariable("id") long id) {
        this.userService.deleteUserById(id);
        return "delete user" + id;
    }

    @PutMapping("/user")
    public User updateUser(@RequestBody User userJson) {
        User user = this.userService.fetchUserById(userJson.getId());
        if (user != null) {
            user.setEmail(userJson.getEmail());
            user.setName(userJson.getName());
            user.setPassword(userJson.getPassword());

            user = this.userService.saveUser(user);
        }
        return user;
    }
}
