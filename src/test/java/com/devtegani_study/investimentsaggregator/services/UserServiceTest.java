package com.devtegani_study.investimentsaggregator.services;

import com.devtegani_study.investimentsaggregator.controllers.dtos.UserDTO;
import com.devtegani_study.investimentsaggregator.entities.User;
import com.devtegani_study.investimentsaggregator.exceptions.UserAlreadyExistException;
import com.devtegani_study.investimentsaggregator.factories.UserDTOFactory;
import com.devtegani_study.investimentsaggregator.factories.UserFactory;
import com.devtegani_study.investimentsaggregator.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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

   @Captor
   private ArgumentCaptor<UUID> userIdArgumentCaptor;

   UserFactory userFactory = new UserFactory();
   UserDTOFactory userDTOFactory = new UserDTOFactory();

   @Nested
   class CreateUser {
      @Test
      @DisplayName("Should create user with success")
      void shouldCreateUserWithSuccess() {
         // Arrange
         UserDTO userData = userDTOFactory.createUserDTO();
         when(userRepository.findUserByEmail(userEmailArgumentCaptor.capture()))
                 .thenReturn(Optional.empty());

         User user = new User(userData);
         when(userRepository.save(userArgumentCaptor.capture()))
                 .thenReturn(user);

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
         assertEquals(user.getUserName(), userCaptured.getUserName());
         assertEquals(user.getEmail(), userCaptured.getEmail());
         assertEquals(user.getPassword(), userCaptured.getPassword());
      }

      @Test
      @DisplayName("Should throw exception when error occurs")
      void shouldThrowExceptionWhenErrorOccurs() {
         //         Arrange
         UserDTO userData = userDTOFactory.createUserDTO();
         User existingUser = new User(userData);
         when(userRepository.findUserByEmail(userData.email()))
                 .thenReturn(Optional.of(existingUser));

         // Act & Assert
         assertThrows(UserAlreadyExistException.class, () -> userService.createUser(userData));
         verify(userRepository).findUserByEmail(userData.email());
         verify(userRepository, never()).save(any(User.class));
      }
   }

   @Nested
   class GetUserById {
      @Test
      @DisplayName("Should get user by id with success")
      void shouldGetUserByIdWithSuccess() {
//         ARRANGE
         User user = userFactory.createUser();

         when(userRepository.findById(userIdArgumentCaptor.capture()))
                 .thenReturn(Optional.of(user));

//         ACT
         User responseUser = userService
                 .getUserById(user.getUserId().toString());

//         ASSERT
         UUID userIdCaptured = userIdArgumentCaptor.getValue();
         verify(userRepository, times(1)).findById(userIdCaptured);
         assertNotNull(responseUser);
         assertEquals(user.getUserId(), userIdCaptured);
         assertEquals(user.getUserName(), responseUser.getUserName());
         assertEquals(user.getEmail(), responseUser.getEmail());
         assertEquals(user.getPassword(), responseUser.getPassword());
      }

      @Test
      @DisplayName("Should throw EntityNotFoundException when error occurs")
      void shouldThrowEntityNotFoundExceptionWhenErrorOccurs() {
//         ARRANGE
         UUID userId = UUID.randomUUID();

         when(userRepository.findById(userIdArgumentCaptor.capture()))
                 .thenReturn(Optional.empty());

//         ACT & ASSERT
         assertThrows(
                 EntityNotFoundException.class
                 , () -> userService
                         .getUserById(userId.toString())
         );
         UUID userIdCaptured = userIdArgumentCaptor.getValue();
         verify(userRepository, times(1)).findById(userIdCaptured);
         assertEquals(userId, userIdCaptured);
      }
   }
   
   @Nested
   class FindAllUsers {
      @Test
      @DisplayName("Should get all users with success")
      void shouldGetAllUsersWithSuccess() {
//         ARRANGE
         List<User> users = userFactory.createListOfUsers();
         when(userRepository.findAll()).thenReturn(users);
//         ACT
         List<User> responseUsers = userService.findAllUsers();

//         ASSERT
         verify(userRepository, times(1)).findAll();
         assertEquals(users.size(), responseUsers.size());
         assertNotNull(responseUsers);
      }
   }

   @Nested
   class DeleteUserById {
      @Test
      @DisplayName("Should delete user by id when user exist")
      void shouldDeleteUserByIdWhenUserExist() {
//         ARRANGE
         UUID userId = UUID.randomUUID();
         when(userRepository.existsById(userIdArgumentCaptor.capture()))
                 .thenReturn(true);
         doNothing()
                 .when(userRepository)
                 .deleteById(userIdArgumentCaptor.capture());

//         ACT
         userService.deleteUserById(userId.toString());

//         ASSERT
         List<UUID> userIdCaptured = userIdArgumentCaptor.getAllValues();
         verify(userRepository, times(1))
                 .existsById(userIdCaptured.get(0));
         verify(userRepository, times(1))
                 .deleteById(userIdCaptured.get(1));
         assertEquals(userIdCaptured.get(0), userIdCaptured.get(1));
         assertEquals(userId, userIdCaptured.get(0));
         assertEquals(userId, userIdCaptured.get(1));
      }

      @Test
      @DisplayName("Should throw EntityNotFoundException when user doesn't exist")
      void shouldThrowEntityNotFoundExceptionWhenUserDoesNotExist() {
//         ARRANGE
         UUID userId = UUID.randomUUID();
         when(userRepository.existsById(userIdArgumentCaptor.capture()))
                 .thenReturn(false);

//         ACT
         assertThrows(
                 EntityNotFoundException.class
                 , () -> userService
                         .deleteUserById(userId.toString())
         );

//         ASSERT
         UUID userIdCaptured = userIdArgumentCaptor.getValue();
         verify(userRepository, times(1))
                 .existsById(userIdCaptured);
         verify(userRepository, never())
                 .deleteById(any());
         assertEquals(userId, userIdCaptured);
      }
   }
   
   @Nested
   class UpdateUserById {
      @Test
      @DisplayName("Should update user with success")
      void shouldUpdateUserWithSuccess() {
//         ARRANGE
         User user = userFactory.createUser();
         UserDTO userData = userDTOFactory.createUserDTO();

         when(userRepository.findById(userIdArgumentCaptor.capture()))
                 .thenReturn(Optional.of(user));
         when(userRepository.findUserByEmail(userEmailArgumentCaptor.capture()))
                 .thenReturn(Optional.empty());
         when(userRepository.save(userArgumentCaptor.capture()))
                 .thenReturn(user);

//         ACT
         User userResponse = userService.updateUserById(
                 user.getUserId().toString(),
                 userData
         );

//         ASSERT
         UUID userIdCaptured = userIdArgumentCaptor.getValue();
         String userEmailCaptured = userEmailArgumentCaptor.getValue();
         User userCaptured = userArgumentCaptor.getValue();

         verify(userRepository, times(1))
                 .findById(userIdCaptured);
         verify(userRepository, times(1))
                 .findUserByEmail(userEmailCaptured);
         verify(userRepository, times(1))
                 .save(userCaptured);

         assertNotNull(userResponse);
         assertEquals(userCaptured.getUserName(), userResponse.getUserName());
         assertEquals(userCaptured.getEmail(), userResponse.getEmail());
         assertEquals(userCaptured.getPassword(), userResponse.getPassword());
         assertEquals(userCaptured.getUserId(), userResponse.getUserId());
         assertEquals(userCaptured.getCreationTimestamp(), userResponse.getCreationTimestamp());
         assertEquals(userCaptured.getUpdateTimestamp(), userResponse.getUpdateTimestamp());
         assertEquals(user.getUserId(), userCaptured.getUserId());
         assertEquals(user.getCreationTimestamp(), userCaptured.getCreationTimestamp());
         assertEquals(user.getUserName(), userCaptured.getUserName());
         assertEquals(user.getEmail(), userCaptured.getEmail());
         assertEquals(user.getPassword(), userCaptured.getPassword());
         assertEquals(user.getUpdateTimestamp(), userCaptured.getUpdateTimestamp());
         assertEquals(user.getUserId(), userResponse.getUserId());
         assertEquals(user.getCreationTimestamp(), userResponse.getCreationTimestamp());
         assertEquals(user.getUserName(), userResponse.getUserName());
         assertEquals(user.getEmail(), userResponse.getEmail());
         assertEquals(user.getPassword(), userResponse.getPassword());
         assertEquals(user.getUpdateTimestamp(), userResponse.getUpdateTimestamp());
         assertEquals(userData.email(), userEmailCaptured);
         assertEquals(userData.email(), userCaptured.getEmail());
         assertEquals(userData.email(), user.getEmail());
         assertEquals(userData.email(), userResponse.getEmail());
         assertEquals(userData.username(), user.getUserName());
         assertEquals(userData.password(), user.getPassword());
         assertEquals(userData.username(), userCaptured.getUserName());
         assertEquals(userData.password(), userCaptured.getPassword());
         assertEquals(userData.username(), userResponse.getUserName());
         assertEquals(userData.password(), userResponse.getPassword());
      }

      @Test
      @DisplayName("Should throw EntityNotFoundException when user does not exist")
      void shouldThrowEntityNotFoundExceptionWhenUserDoesNotExist() {
//         ARRANGE
         UUID userId = UUID.randomUUID();
         UserDTO userData = userDTOFactory.createUserDTO();

         when(userRepository.findById(userIdArgumentCaptor.capture()))
                 .thenReturn(Optional.empty());

//         ACT & ASSERT
         assertThrows(
                 EntityNotFoundException.class
                 , () -> userService
                         .updateUserById(userId.toString(), userData)
         );
         UUID userIdCaptured = userIdArgumentCaptor.getValue();

         verify(userRepository, times(1))
                 .findById(userIdCaptured);
         verify(userRepository, never())
                 .findUserByEmail(any());
         verify(userRepository, never())
                 .save(any());

         assertEquals(userId, userIdCaptured);
      }

      @Test
      @DisplayName("Should throw UserAlreadyExistException when user email is found")
      void shouldThrowUserAlreadyExistExceptionWhenUserEmailIsFound() {
//         ARRANGE
         User user = userFactory.createUser();
         UserDTO userData = userDTOFactory.createUserDTO();

         when(userRepository.findById(userIdArgumentCaptor.capture()))
                 .thenReturn(Optional.of(user));
         when(userRepository.findUserByEmail(userEmailArgumentCaptor.capture()))
                 .thenReturn(Optional.of(user));

//         ACT & ASSERT
         assertThrows(
                 UserAlreadyExistException.class
                 , () -> userService
                         .updateUserById(user.getUserId().toString(), userData)
         );

         UUID userIdCaptured = userIdArgumentCaptor.getValue();
         String userEmailCaptured = userEmailArgumentCaptor.getValue();

         verify(userRepository, times(1))
                 .findById(userIdCaptured);
         verify(userRepository, times(1))
                 .findUserByEmail(userEmailCaptured);
         verify(userRepository, never())
                 .save(any());

         assertEquals(user.getUserId(), userIdCaptured);
         assertEquals(userData.email(), userEmailCaptured);
      }
   }

}
