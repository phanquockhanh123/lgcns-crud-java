package org.example.socialmediaspring.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String address;
    private String email;
    private String phone;
    private String role;
}
