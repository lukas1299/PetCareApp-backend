package com.project.project.main.model;

import lombok.Builder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Builder
public record UserRequest(
        @Pattern(regexp = ".*@gmail[.]com", message = "Invalid email domain!")
        @Email(message = "Email address not valid!")
        @NotEmpty(message = "Email address cannot be empty/null!") String email,

        @Pattern(regexp =
                "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,32}[a-zA-Z0-9]$", message = """
                Username requirements:
                                                  
                at least 3 characters and at most 32 characters,
                consists of alphanumeric characters (a-zA-Z0-9) allowed of the (._-),
                (._-) must not be the first or last character,
                (._-) does not appear consecutively.
                """)
        @NotEmpty(message = "Username cannot be empty/null!") String username,

        @Pattern(regexp =
                "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,30}$", message = """
                Password requirements:
                                              
                at least 8 characters and at most 30 characters,
                at least one digit,
                at least one upper case alphabet,
                at least one lower case alphabet,
                at least one special character,
                cannot contain any whitespace.
                """)
        @NotEmpty(message = "Password cannot be empty/null!") String password,
        RoleType role) {

}
