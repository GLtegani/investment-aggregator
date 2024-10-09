package com.devtegani_study.investimentsaggregator.factories;

import com.devtegani_study.investimentsaggregator.entities.User;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
public class UserFactory {
    public final User createUser() {
        return new User(
                UUID.randomUUID(),
                "user test",
                "example@example.com",
                "123",
                Instant.now(),
                null
        );
    }

    public final User createUserWithAnExistingId(String userId) {
        return new User(
                UUID.fromString(userId),
                "UserNameTest",
                "example@example.com",
                "123",
                Instant.now(),
                null
        );
    }

    public final List<User> createListOfUsers() {
        User[] users = new User[5];

        for(int i = 0; i < users.length; i++) {
            users[i] = this.createUser();
        }
        return Arrays.stream(users).toList();
    }
}
