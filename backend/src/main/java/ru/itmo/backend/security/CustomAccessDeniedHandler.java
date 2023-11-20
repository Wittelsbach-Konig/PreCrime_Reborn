package ru.itmo.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    public CustomAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("message", "Forbidden Error: " + e.getMessage());
        errorDetails.put("timestamp", new Date());
        errorDetails.put("path", httpServletRequest.getRequestURI());
        errorDetails.put("status code", HttpStatus.FORBIDDEN.value());
        OutputStream out = httpServletResponse.getOutputStream();
        objectMapper.writeValue(out, errorDetails);
        out.flush();
    }
}
