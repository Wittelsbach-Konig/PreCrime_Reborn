package ru.itmo.precrimeupd.exceptions;

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
        ErrorObject errorObject = new ErrorObject();
        errorObject.setMethodName(getMethodName(ex));
        errorObject.setMessage(ex.getMessage());
        errorObject.setStatusCode(HttpStatus.BAD_REQUEST.value());
        errorObject.setTimestamp(new Date());
        errorObject.setPath(request.getDescription(false));

        return new ResponseEntity<>(errorObject, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorObject> handleGlobalException(Exception ex, WebRequest request) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setMessage("Internal Server Error");
        errorObject.setPath(request.getDescription(false));
        errorObject.setTimestamp(new Date());
        errorObject.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorObject.setMethodName(getMethodName(ex));

        return new ResponseEntity<>(errorObject, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private String getMethodName(Exception ex) {
        StackTraceElement[] stackTraceElements = ex.getStackTrace();
        if (stackTraceElements != null && stackTraceElements.length > 0) {
            return stackTraceElements[0].getMethodName();
        }
        return "N/A";
    }
}