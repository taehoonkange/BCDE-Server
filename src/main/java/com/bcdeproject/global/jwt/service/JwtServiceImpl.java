package com.bcdeproject.global.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.bcdeproject.domain.member.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
@Setter(value = AccessLevel.PRIVATE)
public class JwtServiceImpl implements JwtService{

    /**
     * @Value를 사용하여 applicaton-jwt.yml 파일의 값들을 필드 값으로 사용
     */
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.access.expiration}")
    private long accessTokenValidityInSeconds;
    @Value("${jwt.refresh.expiration}")
    private long refreshTokenValidityInSeconds;
    @Value("${jwt.access.header}")
    private String accessHeader;
    @Value("${jwt.refresh.header}")
    private String refreshHeader;


    /**
     * JWT의 Subject와 Claim으로 username 사용 -> 클레임의 name을 "username"으로 설정
     * JWT의 헤더에 들어오는 값 : 'Authorization = Bearer {토큰}' 형식
     */
    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String USERNAME_CLAIM = "username";
    private static final String BEARER = "Bearer ";


    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;


    /**
     * AccessToken 생성
     */
    @Override
    public String createAccessToken(String username) {
        return JWT.create() // JWT 토큰을 생성하는 빌더 반환
                .withSubject(ACCESS_TOKEN_SUBJECT) // JWT의 Subject 지정 -> AccessToken이므로 AccessToken
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenValidityInSeconds * 1000)) // 토큰 만료 시간 설정

                //클레임으로는 저희는 username 하나만 사용합니다.
                //추가적으로 식별자나, 이름 등의 정보를 더 추가하셔도 됩니다.
                //추가하실 경우 .withClaim(클래임 이름, 클래임 값) 으로 설정해주시면 됩니다
                .withClaim(USERNAME_CLAIM, username)
                .sign(Algorithm.HMAC512(secret)); // HMAC512 알고리즘 사용, application-jwt.yml에서 지정한 secret 키로 암호화
    }

    /**
     * RefreshToken 생성
     * RefreshToken은 Claim에 username도 넣지 않으므로 withClaim() X
     */
    @Override
    public String createRefreshToken() {
        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenValidityInSeconds * 1000))
                .sign(Algorithm.HMAC512(secret));
    }

    /**
     * RefreshToken DB 저장
     */
    @Override
    public void updateRefreshToken(String username, String refreshToken) {
        memberRepository.findByUsername(username)
                .ifPresentOrElse(
                        member -> member.updateRefreshToken(refreshToken),
                        () -> new Exception("회원이 없습니다")
                );
    }


    /**
     * RefreshToken 삭제
     */
    @Override
    public void destroyRefreshToken(String username) {
        memberRepository.findByUsername(username)
                .ifPresentOrElse(
                        member -> member.destroyRefreshToken(),
                        () -> new Exception("회원이 없습니다")
                );
    }

    /**
     * AccessToken + RefreshToken 헤더에 실어서 보내기
     */
    @Override
    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken){
        response.setStatus(HttpServletResponse.SC_OK);

        setAccessTokenHeader(response, accessToken);
        setRefreshTokenHeader(response, refreshToken);


        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put(ACCESS_TOKEN_SUBJECT, accessToken);
        tokenMap.put(REFRESH_TOKEN_SUBJECT, refreshToken);

    }

    /**
     * AccessToken 헤더에 실어서 보내기
     */
    @Override
    public void sendAccessToken(HttpServletResponse response, String accessToken){
        response.setStatus(HttpServletResponse.SC_OK);

        setAccessTokenHeader(response, accessToken);


        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put(ACCESS_TOKEN_SUBJECT, accessToken);
    }


    /**
     * 헤더에서 AccessToken 추출
     */
    @Override
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader)).filter(

                accessToken -> accessToken.startsWith(BEARER)

        ).map(accessToken -> accessToken.replace(BEARER, ""));
    }
    /**
     * 헤더에서 RefreshToken 추출
     */
    @Override
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshHeader)).filter(

                refreshToken -> refreshToken.startsWith(BEARER)

        ).map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    /**
     * AccessToken에서 Username 추출
     */
    @Override
    public Optional<String> extractUsername(String accessToken) {
        try {
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secret))
                    //토큰의 서명의 유효성을 검사하는데 사용할 알고리즘이 있는
                    //JWT verifier builder를 반환합니다

                    .build() //반환된 빌더로 JWT verifier를 생성합니다

                    .verify(accessToken) //accessToken을 검증하고 유효하지 않다면 예외를 발생시킵니다.
                    .getClaim(USERNAME_CLAIM) //claim을 가져옵니다
                    .asString());
        } catch (Exception e) {
            log.error(e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * AccessToken 헤더 설정
     */
    @Override
    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(accessHeader, accessToken);
    }

    /**
     * RefreshToken 헤더 설정
     */
    @Override
    public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
        response.setHeader(refreshHeader, refreshToken);
    }

    /**
     * Token이 유효한지 검증
     */
    @Override
    public boolean isTokenValid(String token){
        try {
            JWT.require(Algorithm.HMAC512(secret)).build().verify(token);
            return true;
        }catch (Exception e){
            log.error("유효하지 않은 Token입니다", e.getMessage());
            return false;
        }
    }
}