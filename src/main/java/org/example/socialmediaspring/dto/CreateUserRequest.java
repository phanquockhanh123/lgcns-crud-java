package org.example.socialmediaspring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String address;
    private String email;
}
