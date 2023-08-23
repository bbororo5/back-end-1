package com.example.sixneek.security.jwt;

import com.example.sixneek.global.dto.ApiResponseDto;
import com.example.sixneek.global.exception.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException {

        try {
            filterChain.doFilter(request, response);
        } catch (CustomException e){
            setResponse(response, e.getStatus(), e.getMessage());
        } catch (Exception e) {
            setResponse(response, HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    private void setResponse(HttpServletResponse response, HttpStatus status, String message){
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        ApiResponseDto<?> responseDto = ApiResponseDto.builder()
                .status(status)
                .message(message)
                .build();
        try{
            response.getWriter().write(objectMapper.writeValueAsString(responseDto));
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
