package ru.itmo.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDto {
    @NotNull
    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Invalid login format")
    @Size(max = 25)
    private String login;

    @NotNull
    @NotEmpty
    private String password;

    @NotNull
    @NotEmpty
    private String confirmPassword;

    @Pattern(regexp = "^$|^\\S+@\\S+$", message = "Invalid email format")
    private String email;

    @NotNull
    @NotEmpty
    @Size(max = 25)
    //@Pattern(regexp = "[a-zA-Z]", message = "FirstName should contain only letters")
    private String firstName;

    @NotNull
    @NotEmpty
    @Size(max = 25)
    //@Pattern(regexp = "[a-zA-Z]", message = "LastName should contain only letters")
    private String lastName;

    @NotNull
    @NotEmpty
    private List<String> roles;

    private int telegramId;
}
