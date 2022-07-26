package com.bcdeproject.global.login.controller;

import com.bcdeproject.global.login.dto.LoginDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class LoginController {

    // Login 로직은 JsonUsernamePasswordAuthenticationFilter에서 진행
    // Swagger용 API
    @Operation(summary = "로그인 API", description = "로그인 API Example")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "500", description = "서버 에러"),
    })
    @PostMapping("/login")
    public void login(@RequestBody LoginDto loginDto) {
        log.info("요청 로그인 username : {}", loginDto.getUsername());
        log.info("요청 로그인 password : {}", loginDto.getPassword());
    }
}