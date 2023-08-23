package com.example.sixneek.security.jwt;

import com.example.sixneek.global.dto.ApiResponseDto;
import com.example.sixneek.member.dto.LoginRequestDto;
import com.example.sixneek.security.UserDetailsImpl;
import com.example.sixneek.security.entity.RefreshToken;
import com.example.sixneek.security.repository.RefreshTokenRedisRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;
    private final RefreshTokenRedisRepository redisRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, RefreshTokenRedisRepository redisRepository) {
        this.jwtUtil = jwtUtil;
        this.redisRepository = redisRepository;
        setFilterProcessesUrl("/api/members/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getEmail(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        String email = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();

        // 토큰 발급
        String accessToken = jwtUtil.createAccessToken(email);
        String refreshToken = jwtUtil.createRefreshToken();

        // access 토큰은 응답 헤더에 추가
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, JwtUtil.BEARER_PREFIX + accessToken);

        // refresh 토큰은 redis에 저장
        RefreshToken refresh = RefreshToken.builder()
                .id(email)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        redisRepository.save(refresh);

        response.setStatus(200);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        ApiResponseDto<?> responseDto = ApiResponseDto.builder()
                .status(HttpStatus.OK)
                .message("로그인 성공")
                .build();
        String result = new ObjectMapper().writeValueAsString(responseDto);
        response.getWriter().write(result);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setStatus(401);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        ApiResponseDto<?> responseDto = ApiResponseDto.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("로그인 실패")
                .build();
        String result = new ObjectMapper().writeValueAsString(responseDto);
        response.getWriter().write(result);
    }

}