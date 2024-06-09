package org.example.socialmediaspring.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeneralResponse<D> {
    private boolean success;
    private String source;
    private String errorCode;
    private String traceId;
    private String message;
    private D data;

    public static <T> GeneralResponse<T> createResponse(T data) {
        GeneralResponse<T> response = new GeneralResponse<>();
        response.setData(data);
        return response;
    }
}
