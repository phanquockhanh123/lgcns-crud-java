package org.example.socialmediaspring.dto.auth;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthRes {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String address;
    private String phone;
}
