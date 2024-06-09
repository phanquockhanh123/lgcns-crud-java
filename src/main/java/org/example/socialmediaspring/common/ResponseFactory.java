package org.example.socialmediaspring.common;

import lombok.extern.slf4j.Slf4j;
import org.example.socialmediaspring.constant.ErrorCodeConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Slf4j
@Component
public class ResponseFactory {
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private Locale defaultLocale;

    public <D> ResponseEntity success() {
        return success(null);
    }
        public <D> ResponseEntity success(D data) {
        var responseObject = GeneralResponse.createResponse(data);
        responseObject.setSuccess(true);
        return ResponseEntity.ok().body(responseObject);
    }

    public <D> ResponseEntity fail(D data) {
        var responseObject = GeneralResponse.createResponse(data);
        responseObject.setSuccess(false);
        return ResponseEntity.ok().body(responseObject);
    }

    public <D> ResponseEntity fail(D data, ErrorCodeConst code, String message) {
        if (message == null || message.isEmpty()) {
            message = code.getMessage();
        }
        var translatedMessage = message;
        try {
            translatedMessage = this.messageSource.getMessage(message, null, defaultLocale);
        } catch (Exception ignore) {
        }
        var responseObject = GeneralResponse.createResponse(data);
        responseObject.setErrorCode(code.getCode());
        responseObject.setMessage(translatedMessage);
        return ResponseEntity.status(code.getHttpCode()).body(responseObject);
    }

}
