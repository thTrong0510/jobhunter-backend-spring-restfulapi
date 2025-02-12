package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/user/create")
    public User createNewUser(@RequestBody User userJson) {
        // User user = new User();
        // user.setEmail("vthanhtrongng@gmail.com");
        // user.setName("Thanh Trong");
        // user.setPassword("Thanhtrong@0510");

        User user = this.userService.saveUser(userJson);
        return user;
    }
}
