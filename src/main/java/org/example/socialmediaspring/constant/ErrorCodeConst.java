package org.example.socialmediaspring.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCodeConst {
    REQUEST_LOCKED("request.locked.error", HttpStatus.BAD_REQUEST.value(), "request.locked.error"),
    INTERNAL_SERVER_ERROR("internal.server.error", HttpStatus.INTERNAL_SERVER_ERROR.value(), "internal.server.error"),
    UNAUTHORIZED("unauthorized", HttpStatus.UNAUTHORIZED.value(), "unauthorized"),
    PERMISSION_DENIED("permission.denied", HttpStatus.FORBIDDEN.value(), "permission.denied"),
    INVALID_INPUT("invalid.input", HttpStatus.BAD_REQUEST.value(), "invalid.input"),
    EXCHANGE_ERROR("exchange.error", HttpStatus.INTERNAL_SERVER_ERROR.value(), "exchange.error"),
    NOT_SUPPORTED_METHOD("not.supported.method", HttpStatus.METHOD_NOT_ALLOWED.value(), "not.supported.method"),
    NOT_SUPPORTED_MEDIA_TYPE("not.supported.media.type", HttpStatus.BAD_REQUEST.value(), "not.supported.media.type"),
    NOT_FOUND_CLIENT("not.found.client", HttpStatus.BAD_REQUEST.value(), "not.found.client"),
    CLIENT_NOT_ACTIVE("client.not.active", HttpStatus.BAD_REQUEST.value(), "client.not.active"),
    MISSING_REQUEST_PARAM("missing.request.param", HttpStatus.BAD_REQUEST.value(),"missing.request.param"),
    REQUEST_PARAM_TYPE_MISMATCH("request.param.type.mismatch", HttpStatus.BAD_REQUEST.value(),"request.param.type.mismatch"),
    CONSTRAINT_VIOLATION("constraint.violation", HttpStatus.BAD_REQUEST.value(),"constraint.violation"),
    MISSING_REQUEST_HEADER("missing.request.header", HttpStatus.BAD_REQUEST.value(),"missing.request.header"),
    VALIDATE_VIOLATION("validate.violation", HttpStatus.OK.value(), "validate.violation"),
    NOT_FOUND_PERMISSION("not.found.permission", HttpStatus.NOT_FOUND.value(), "not.found.permission"),
    PERMISSION_EXISTS("permission.exists", HttpStatus.BAD_REQUEST.value(), "permission.exists"),
    ACTION_RESOURCE_EXISTS("action.resource.exists", HttpStatus.BAD_REQUEST.value(), "action.resource.exists"),
    CANNOT_DELETE_PERMISSION("cannot.delete.permission", HttpStatus.BAD_REQUEST.value(), "cannot.delete.permission"),
    NOT_FOUND_ROLE("not.found.role", HttpStatus.NOT_FOUND.value(), "not.found.role"),
    CANNOT_DELETE_ROLE("cannot.delete.role", HttpStatus.BAD_REQUEST.value(), "cannot.delete.role"),
    ROLE_EXISTS("role.exists", HttpStatus.BAD_REQUEST.value(), "role.exists"),
    PERMISSION_IDS_NULL("permission.ids.null", HttpStatus.BAD_REQUEST.value(), "permission.ids.null"),
    ;

    private final String code;
    private final int httpCode;
    private final String message;


    @Override
    public String toString() {
        return "ResponseStatus{" +
                "code='" + code + '\'' +
                "httpCode='" + httpCode + '\'' +
                '}';
    }
}
