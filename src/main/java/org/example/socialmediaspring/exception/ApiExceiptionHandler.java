package org.example.socialmediaspring.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.example.socialmediaspring.common.GeneralResponse;
import org.example.socialmediaspring.common.ResponseFactory;
import org.example.socialmediaspring.constant.ErrorCodeConst;
import org.example.socialmediaspring.utils.CustomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ApiExceiptionHandler {
    @Autowired
    private ResponseFactory responseFactory;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private Locale defaultLocale;

    @Autowired
    private CustomStringUtils customStringUtils;

    @ExceptionHandler({BizException.class})
    public @ResponseBody
    ResponseEntity<GeneralResponse<Object>> handleAllException(BizException ex, HttpServletRequest request, HttpServletResponse response) {
        HttpStatus status = ex.getHttpStatus() == null ? HttpStatus.BAD_REQUEST : HttpStatus.valueOf(ex.getHttpStatus());
        this.logException(ex.getMessage(), status.value(), request, ex);
        RestError restError = ex.transformToRestError(messageSource, defaultLocale);

        GeneralResponse<Object> resp = new GeneralResponse<>();
        resp.setSource(ex.getSource());
        resp.setErrorCode(ex.getErrorCode());
        resp.setMessage(restError.getMessage());

        return new ResponseEntity<>(resp, status);
    }

    @ExceptionHandler(Exception.class)
    public @ResponseBody
    ResponseEntity<GeneralResponse<Object>> handleAllException(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        this.logException("An unknown error has occurred", ErrorCodeConst.INTERNAL_SERVER_ERROR.getHttpCode(), request, ex);

        return responseFactory.fail(null, ErrorCodeConst.INTERNAL_SERVER_ERROR, null);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public @ResponseBody
    ResponseEntity<GeneralResponse<Object>> handleNotSupportedMethodException(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        this.logException("Not supported method", ErrorCodeConst.NOT_SUPPORTED_METHOD.getHttpCode(), request, ex);
        return responseFactory.fail(null, ErrorCodeConst.NOT_SUPPORTED_METHOD, null);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public @ResponseBody
    ResponseEntity<GeneralResponse<Void>> handleNotReadableException(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        this.logException("Not readable exception", ErrorCodeConst.INVALID_INPUT.getHttpCode(), request, ex);
        return responseFactory.fail(null, ErrorCodeConst.INVALID_INPUT, null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ResponseEntity<GeneralResponse<Object>> handleValidationExceptions(
            BindException ex, HttpServletRequest request, HttpServletResponse response) {
        this.logException("Invalid input", ErrorCodeConst.INVALID_INPUT.getHttpCode(), request, ex);
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String defaultMessage = error.getDefaultMessage();
            String translatedMessage = this.messageSource.getMessage(defaultMessage, null, defaultMessage, defaultLocale);
            errors.put(fieldName, translatedMessage);
        });
        return responseFactory.fail(errors, ErrorCodeConst.INVALID_INPUT, null);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public @ResponseBody
    ResponseEntity<GeneralResponse<Void>> handleAccessDeniedException(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        this.logException("Permission denied", ErrorCodeConst.PERMISSION_DENIED.getHttpCode(), request, ex);
        return responseFactory.fail(null, ErrorCodeConst.PERMISSION_DENIED, null);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public @ResponseBody
    ResponseEntity<GeneralResponse<Void>> handleNotSupportedMediaTypeException(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        this.logException("Not support media type", ErrorCodeConst.NOT_SUPPORTED_MEDIA_TYPE.getHttpCode(), request, ex);
        return responseFactory.fail(null, ErrorCodeConst.NOT_SUPPORTED_MEDIA_TYPE, null);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public @ResponseBody
    ResponseEntity<GeneralResponse<Object>> handleMissingServletRequestParameterException(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        this.logException("Missing server request parameter exception", ErrorCodeConst.MISSING_REQUEST_PARAM.getHttpCode(), request, ex);

        return responseFactory.fail(null, ErrorCodeConst.MISSING_REQUEST_PARAM, null);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public @ResponseBody
    ResponseEntity<GeneralResponse<Object>> handleMethodArgumentTypeMismatchException(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        this.logException("Method argument type mismatch exception", ErrorCodeConst.REQUEST_PARAM_TYPE_MISMATCH.getHttpCode(), request, ex);

        MethodArgumentTypeMismatchException exception = (MethodArgumentTypeMismatchException) ex;
        String message = String.format(customStringUtils.transformToRestError("request.param.mismatch"), exception.getName());
        return responseFactory.fail(null, ErrorCodeConst.REQUEST_PARAM_TYPE_MISMATCH, message);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public @ResponseBody
    ResponseEntity<GeneralResponse<Object>> handleMissingRequestHeaderException(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        this.logException("Missing request header exception", ErrorCodeConst.MISSING_REQUEST_HEADER.getHttpCode(), request, ex);

        return responseFactory.fail(null, ErrorCodeConst.MISSING_REQUEST_HEADER, null);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public @ResponseBody
    ResponseEntity<GeneralResponse<Object>> handleConstraintViolationException(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        this.logException("Constraint violation exception", ErrorCodeConst.CONSTRAINT_VIOLATION.getHttpCode(), request, ex);

        return responseFactory.fail(null, ErrorCodeConst.CONSTRAINT_VIOLATION, ex.getMessage());
    }

    private void logException(String msg, int httpStatus, HttpServletRequest request, Exception ex) {
        log.error(msg, ex);
    }

    @ExceptionHandler(ValidationException.class)
    public @ResponseBody
    ResponseEntity<GeneralResponse<Object>> handleValidationException(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        this.logException("Validate violation exception", ErrorCodeConst.VALIDATE_VIOLATION.getHttpCode(), request, ex);

        return responseFactory.fail(null, ErrorCodeConst.VALIDATE_VIOLATION, ex.getMessage());
    }


}
