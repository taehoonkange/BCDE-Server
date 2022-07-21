package com.bcdeproject.global.jwt.filter;

import com.bcdeproject.domain.member.Member;
import com.bcdeproject.domain.member.repository.MemberRepository;
import com.bcdeproject.global.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Jwt 인증 필터
 * 1. 둘 다 유효한 경우 -> AccessToken재발급, 인증은 진행하지 않음.
 * 2. RefreshToken은 유효하고, AccessToken은 없거나 유효하지 않은 경우 -> AccessToken 재발급
 * 3. RefreshToken은 없거나 유효하지 않고, AccessToken은 유효한 경우 -> 인증은 성공되나, RefreshToken을 재발급하지는 않음
 * 4. RefreshToken과 AccessToken 모두 없거나 유효하지 않은 경우 -> 인증에 실패합니다. 403을 제공
 */
@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    private final String NO_CHECK_URL = "/login"; // /login으로 들어오는 요청은 Filter 작동 X

    /**
     * 1. 리프레시 토큰이 오는 경우 -> 유효하면 AccessToken 재발급후, 필터 진행 X, 바로 튕기기
     *
     * 2. 리프레시 토큰은 없고 AccessToken만 있는 경우 -> 유저정보 저장후 필터 계속 진행
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getRequestURI().equals(NO_CHECK_URL)) {
            filterChain.doFilter(request, response);
            return;//안해주면 아래로 내려가서 계속 필터를 진행해버림
        }

        String refreshToken = jwtService.extractRefreshToken(request).filter(jwtService::isTokenValid).orElse(null); // RefreshToken이 없거나 유효하지 않다면 null을 반환

        // refreshToken이 유효하다면 해당 refreshToken을 가진 유저정보를 찾아오고, 존재한다면 AccessToken을 재발급(checkRefreshTokenAndReIssueAccessToken() 메소드)
        if(refreshToken != null){
            checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
            return; // 바로 return 하는 이유는, refreshToken만 보낸 경우에는 인증 처리를 하지않기 위해서 AccessToken 재발급 후 필터에서 튕기기
        }

        // refreshToken이 없다면 AccessToken을 검사하는 로직을 수행
        checkAccessTokenAndAuthentication(request, response, filterChain);

    }



    // request에서 AccessToken을 추출한 후, 있다면 해당 AccessToken에서 username을 추출
    // username이 추출되었다면 해당 회원을 찾아와서 그 정보를 가지고 인증처리
    // 이때 SecurityContextHolder에 Authentication 객체를 만들어 반환하는데, NullAuthoritiesMapper가 쓰인다.
    private void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        jwtService.extractAccessToken(request).filter(jwtService::isTokenValid).ifPresent(

                accessToken -> jwtService.extractUsername(accessToken).ifPresent(

                        username -> memberRepository.findByUsername(username).ifPresent(

                                this::saveAuthentication
                        )
                )
        );

        filterChain.doFilter(request,response);
    }



    private void saveAuthentication(Member member) {
        UserDetails user = User.builder()
                .username(member.getUsername())
                .password(member.getPassword())
                .roles(member.getRole().name())
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null,authoritiesMapper.mapAuthorities(user.getAuthorities()));


        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    private void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {


        memberRepository.findByRefreshToken(refreshToken).ifPresent(
                member -> jwtService.sendAccessToken(response, jwtService.createAccessToken(member.getUsername()))
        );


    }
}
