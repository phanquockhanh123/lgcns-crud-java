package org.example.socialmediaspring.dto.user;

import lombok.*;
import org.example.socialmediaspring.entity.Role;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SearchUserRequest {
    private Integer limit = 20;
    private Integer page = 1;
    private Boolean getTotalCount;
    private String email;
    private Integer id;
    private Role role;
}
