package com.devtegani_study.investimentsaggregator.services;

import com.devtegani_study.investimentsaggregator.controllers.dtos.UserDTO;
import com.devtegani_study.investimentsaggregator.entities.User;
import com.devtegani_study.investimentsaggregator.exceptions.UserAlreadyExistException;
import com.devtegani_study.investimentsaggregator.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public final User createUser(UserDTO userData) {
        Optional<User> userExist = this.repository.findUserByEmail(userData.email());
        if(userExist.isPresent()) {
            throw new UserAlreadyExistException();
        } else {
            User user = new User(userData);
            return this.repository.save(user);
        }
    }

    public final User getUserById(String userId) {
        return this.repository.findById(UUID.fromString(userId)).orElseThrow(EntityNotFoundException::new);
    }

    public final List<User> findAllUsers() {
        return this.repository.findAll();
    }

    public final void deleteUserById(String userId) {
        boolean userExist = this.repository.existsById(UUID.fromString(userId));

        if(userExist) {
            this.repository.deleteById(UUID.fromString(userId));
        } else {
            throw new EntityNotFoundException();
        }
    }

    public final User updateUserById(
            String userId,
            UserDTO userData
    ) {
        Optional<User> user = this.repository.findById(UUID.fromString(userId));
        if(user.isPresent()) {
            if(user.get().getUserName() != null) {
                user.get().setUserName(userData.username());
            }

            if(user.get().getEmail() != null) {
                Optional<User> userEmail = this.repository.findUserByEmail(userData.email());

                if(userEmail.isPresent()) {
                    throw new UserAlreadyExistException();
                } else {
                    user.get().setEmail(userData.email());
                }
            }

            if(user.get().getPassword() != null) {
                user.get().setPassword(userData.password());
            }

            this.repository.save(user.get());
            return user.get();
        } else {
            throw new EntityNotFoundException();
        }
    }
}
