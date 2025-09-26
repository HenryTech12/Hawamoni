package com.hawamoni.app.moni.handler;

import com.hawamoni.app.moni.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class AppExceptionHandler {

    @Autowired
    private ErrorResponse errorResponse;

    @ExceptionHandler(Exception.class)
    public Map<String,Object> handleAllExceptions(Exception e, HttpServletRequest request) {
        return errorResponse.configureError(request,e);
    }
}
