package org.example.socialmediaspring.dto.user;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserResponse {
    Long id;
    String firstName;
    String lastName;
    String userName;
    String address;
    String email;
    String phone;
    String role;
}