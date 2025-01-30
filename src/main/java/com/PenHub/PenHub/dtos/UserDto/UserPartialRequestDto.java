package com.PenHub.PenHub.dtos.UserDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPartialRequestDto {



    @Size(min = 3,max = 20,message = "user name must be between 3 to 20 character")
    private String username;

    @Size(min = 6,max = 15,message = "Password name must be between 6 to 15 character")
    private String password;

    @Email(message = "Email should be valid")
    private String email;

}
