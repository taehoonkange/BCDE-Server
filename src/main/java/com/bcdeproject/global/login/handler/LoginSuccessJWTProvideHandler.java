package com.bcdeproject.global.login.handler;

import com.bcdeproject.domain.member.repository.MemberRepository;
import com.bcdeproject.global.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 로그인 성공 시 처리하는 핸들러
 * 로그인 유저 정보로 AccessToken은 발급만, RefreshToken은 발급 후 DB에 저장
 * 요청 시 헤더에 AccessToken과 RefreshToken 정보를 포함하여 요청
 */
@Slf4j
@RequiredArgsConstructor
public class LoginSuccessJWTProvideHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final MemberRepository memberRepository;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        String username = extractUsername(authentication); // 인증 정보에서 Username 추출
        String accessToken = jwtService.createAccessToken(username); // JwtServiceImpl의 createAccessToken을 사용하여 AccessToken 발급
        String refreshToken = jwtService.createRefreshToken(); // JwtServiceImpl의 createRefreshToken을 사용하여 RefreshToken 발급

        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken); // 헤더에 accessToken, refreshToken을 실어서 요청

        // DB에 refreshToken 저장
        memberRepository.findByUsername(username).ifPresent(
                member -> {
                    member.updateRefreshToken(refreshToken);
                    memberRepository.saveAndFlush(member);
                }
        );


        log.info( "로그인에 성공합니다. username: {}" ,username);
        log.info( "AccessToken 을 발급합니다. AccessToken: {}" ,accessToken);
        log.info( "RefreshToken 을 발급합니다. RefreshToken: {}" ,refreshToken);
    }


    private String extractUsername(Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }
}