package org.example.socialmediaspring.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @NotBlank(message = "First name is required")
    @NotNull(message = "First name is required")
    private String firstName;
    @NotBlank(message = "Last name is required")
    @NotNull(message = "Last name is required")
    private String lastName;
    private String password;
    private String address;

    @NotBlank(message = "Email is required")
    @NotNull(message = "Email is required")
    private String email;

    @NotBlank(message = "Phone is required")
    @NotNull(message = "Phone number is required")
    private String phone;
}
