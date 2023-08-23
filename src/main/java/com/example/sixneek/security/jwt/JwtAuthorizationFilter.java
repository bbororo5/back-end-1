package com.example.sixneek.security.jwt;

import com.example.sixneek.global.exception.CustomException;
import com.example.sixneek.security.UserDetailsServiceImpl;
import com.example.sixneek.security.entity.RefreshToken;
import com.example.sixneek.security.repository.RefreshTokenRedisRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final RefreshTokenRedisRepository redisRepository;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService, RefreshTokenRedisRepository redisRepository) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.redisRepository = redisRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String bearerToken = request.getHeader(JwtUtil.AUTHORIZATION_HEADER);
        String accessToken = jwtUtil.substringToken(bearerToken);

        if (StringUtils.hasText(accessToken)) { // access 토큰이 있으면
            switch (jwtUtil.validateToken(accessToken)) { // 검증
                case DENIED -> throw new CustomException(HttpStatus.UNAUTHORIZED, "유효하지 않은 access 토큰입니다.");

                case EXPIRED -> {
                    // refresh 토큰 찾기
                    RefreshToken refreshToken = redisRepository.findByAccessToken(accessToken)
                            .orElseThrow(() -> new CustomException(HttpStatus.UNAUTHORIZED, "만료된 access 토큰에 대한 refresh 토큰이 만료되었습니다."));
                    if (jwtUtil.validateToken(refreshToken.getRefreshToken()) == JwtUtil.JwtCode.ACCESS) { // refresh 토큰이 유효하다면
                        try {
                            setAuthentication(refreshToken.getId()); // email 저장
                        } catch (Exception e) {
                            log.error(e.getMessage());
                            return;
                        }

                        // access 토큰 재발급
                        log.info("access 토큰 재발급");
                        accessToken = jwtUtil.createAccessToken(refreshToken.getId());
                        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, JwtUtil.BEARER_PREFIX + accessToken);

                        // 재발급한 access 토큰 업데이트
                        refreshToken = RefreshToken.builder()
                                .id(refreshToken.getId())
                                .accessToken(accessToken)
                                .build();
                        redisRepository.save(refreshToken);
                        throw new CustomException(HttpStatus.BAD_REQUEST, "access 토큰이 만료되어 재발급했습니다.");
                    }
                }

                case ACCESS -> {
                    String email = jwtUtil.getUserInfoFromToken(accessToken); // 토큰 정보 추출
                    try {
                        setAuthentication(email); // 사용자 정보 저장
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        return;
                    }
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    // 인증 처리
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}