package org.example.socialmediaspring.dto.auth;

import lombok.*;

@Getter
@Setter
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {
    private String curPass;
    private String newPass;
    private String confirmPass;
}
