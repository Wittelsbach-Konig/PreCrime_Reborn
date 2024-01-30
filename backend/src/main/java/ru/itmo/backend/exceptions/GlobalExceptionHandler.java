package ru.itmo.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorObject> handleNotFoundException(NotFoundException ex, WebRequest request) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setMethodName(getMethodName(ex));
        errorObject.setMessage(ex.getMessage());
        errorObject.setStatusCode(HttpStatus.NOT_FOUND.value());
        errorObject.setTimestamp(new Date());
        errorObject.setPath(request.getDescription(false));

        return new ResponseEntity<>(errorObject, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotValidArgumentException.class)
    public ResponseEntity<ErrorObject> handleNotValidArgumentException(NotValidArgumentException ex, WebRequest request){
        return getErrorObjectResponseEntity(request, getMethodName(ex), ex.getMessage());
    }

    @ExceptionHandler(InvalidRoleException.class)
    public ResponseEntity<ErrorObject> handleInvalidRoleException(InvalidRoleException ex, WebRequest request) {
        return getErrorObjectResponseEntity(request, getMethodName(ex), ex.getMessage());
    }

    private ResponseEntity<ErrorObject> getErrorObjectResponseEntity(WebRequest request, String methodName, String message) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setMethodName(methodName);
        errorObject.setMessage(message);
        errorObject.setStatusCode(HttpStatus.BAD_REQUEST.value());
        errorObject.setTimestamp(new Date());
        errorObject.setPath(request.getDescription(false));

        return new ResponseEntity<>(errorObject, HttpStatus.BAD_REQUEST);
    }

    private String getMethodName(Exception ex) {
        StackTraceElement[] stackTraceElements = ex.getStackTrace();
        if (stackTraceElements != null && stackTraceElements.length > 0) {
            return stackTraceElements[0].getMethodName();
        }
        return "N/A";
    }
}