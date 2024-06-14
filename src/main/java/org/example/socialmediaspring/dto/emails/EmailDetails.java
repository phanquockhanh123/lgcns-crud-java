package org.example.socialmediaspring.dto.emails;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EmailDetails {
    private String recipient;
    private  String messageBody;
    private String subject;
    private String attachment;
}
