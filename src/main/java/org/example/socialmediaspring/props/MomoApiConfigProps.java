package org.example.socialmediaspring.props;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("my.mtn-momo")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MomoApiConfigProps {
    private String targetEnvironment;
    private String ocpApimSubscriptionKey;
    private String apiKey;
    private String apiUserId;
}
