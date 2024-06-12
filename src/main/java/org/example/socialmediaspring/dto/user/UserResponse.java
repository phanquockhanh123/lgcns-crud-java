package org.example.socialmediaspring.dto.user;

import lombok.*;
import org.example.socialmediaspring.entity.Role;

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
    Role role;
}