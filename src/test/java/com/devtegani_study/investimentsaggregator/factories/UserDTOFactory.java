package com.devtegani_study.investimentsaggregator.factories;

import com.devtegani_study.investimentsaggregator.controllers.dtos.UserDTO;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserDTOFactory {
    public final UserDTO createUserDTO() {
        return new UserDTO(
                "username",
                "email@email.com",
                "12345"
        );
    }
}
