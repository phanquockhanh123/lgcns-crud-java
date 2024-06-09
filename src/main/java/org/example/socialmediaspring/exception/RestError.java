package org.example.socialmediaspring.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class RestError implements Serializable {

    private String code;
    private String message;
    private String[] arguments;
    private Long timestamp = System.currentTimeMillis();
    private String exception;
    private String source;

}
