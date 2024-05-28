package org.example.socialmediaspring.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

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
}