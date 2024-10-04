package com.devtegani_study.investimentsaggregator.services;

import com.devtegani_study.investimentsaggregator.controllers.dtos.UserDTO;
import com.devtegani_study.investimentsaggregator.entities.User;
import com.devtegani_study.investimentsaggregator.exceptions.UserAlreadyExistException;
import com.devtegani_study.investimentsaggregator.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
   @Mock
   private UserRepository userRepository;

   @InjectMocks
   private UserService userService;

   @Captor
   private ArgumentCaptor<User> userArgumentCaptor;

   @Captor
   private ArgumentCaptor<String> userEmailArgumentCaptor;

   @Nested
   class CreateUser {
      @Test
      @DisplayName("Should create user with success")
      void shouldCreateUserWithSuccess() {
         // Arrange
         UserDTO userData = new UserDTO(
                 "username",
                 "email@example.com",
                 "12345"
         );
         when(userRepository.findUserByEmail(userEmailArgumentCaptor.capture()))
                 .thenReturn(Optional.empty());

         User user = new User(userData);
         when(userRepository.save(userArgumentCaptor.capture())).thenReturn(user);

         // Act
         User createdUser = userService.createUser(userData);

         // Assert
         User userCaptured = userArgumentCaptor.getValue();
         String userEmailCaptured = userEmailArgumentCaptor.getValue();

         verify(userRepository).findUserByEmail(userEmailCaptured);
         verify(userRepository).save(userCaptured);
         assertNotNull(createdUser);
         assertEquals(userData.email(), userEmailCaptured);
         assertEquals(userData.username(), userCaptured.getUserName());
         assertEquals(userData.email(), userCaptured.getEmail());
         assertEquals(userData.password(), userCaptured.getPassword());
         assertEquals(userCaptured.getEmail(), userEmailCaptured);
      }

      @Test
      @DisplayName("Should throw exception when error occurs")
      void shouldThrowExceptionWhenErrorOccurs() {
         //         Arrange
         UserDTO userData = new UserDTO(
            "username",
            "email@email.com",
            "12345"
         );
         User existingUser = new User(userData);
         doReturn(Optional.of(existingUser)).when(userRepository).findUserByEmail(userData.email());

         // Act & Assert
         assertThrows(UserAlreadyExistException.class, () -> userService.createUser(userData));
         verify(userRepository).findUserByEmail(userData.email());
         verify(userRepository, never()).save(any(User.class));
      }
   }

}
