package com.hawamoni.app.moni.response;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class ErrorResponse {

    public Map<String,Object> configureError(HttpServletRequest request, Exception exception) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("message",exception.getMessage());
        errors.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errors.put("reason",HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        errors.put("error",HttpStatus.INTERNAL_SERVER_ERROR.toString());
        errors.put("path",request.getRequestURI());
        return errors;
    }
}
