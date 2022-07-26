package com.bcdeproject.global.login.controller;

import com.bcdeproject.global.login.dto.LoginDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class LoginController {

    // Login 로직은 JsonUsernamePasswordAuthenticationFilter에서 진행
    // Swagger용 API
    @PostMapping("/login")
    public void login(@RequestBody LoginDto loginDto) {
        log.info("요청 로그인 username : {}", loginDto.getUsername());
        log.info("요청 로그인 password : {}", loginDto.getPassword());
    }
}