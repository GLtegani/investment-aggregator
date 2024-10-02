package com.devtegani_study.investimentsaggregator.controllers;

import com.devtegani_study.investimentsaggregator.controllers.dtos.UserDTO;
import com.devtegani_study.investimentsaggregator.entities.User;
import com.devtegani_study.investimentsaggregator.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public final ResponseEntity<User> createUser(@RequestBody UserDTO userData) {
        User user = this.userService.createUser(userData);
        return ResponseEntity.created(URI.create("/api/users/" + user.getUserId().toString())).body(user);
    }

    @GetMapping("/{userId}")
    public final ResponseEntity<User> getUserById(@PathVariable("userId") String userId) {
        User user = this.userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public final ResponseEntity<List<User>> findAllUsers() {
        List<User> users = this.userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{userId}")
    public final ResponseEntity<User> updateUserById(
            @PathVariable("userId") String userId,
            @RequestBody UserDTO userData
    ) {
        User user = this.userService.updateUserById(userId, userData);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{userId}")
    public final ResponseEntity<Void> deleteUserById(@PathVariable("userId") String userId) {
        this.userService.deleteUserById(userId);
        return ResponseEntity.noContent().build();
    }
}
