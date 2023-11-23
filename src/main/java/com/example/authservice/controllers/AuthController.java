package com.example.authservice.controllers;

import com.example.authservice.dto.AuthRequest;
import com.example.authservice.dto.AuthResponse;
import com.example.authservice.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    private final Integer tokenAge;

    @Autowired
    public AuthController(AuthService authService,
                          @Value("${jwt.expiration}") Integer tokenAge) {
        this.authService = authService;
        this.tokenAge = tokenAge;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest authRequest, HttpServletResponse response){
        AuthResponse authResponse = authService.register(authRequest);
        return ResponseEntity.ok(setCookie(authResponse, response));
    }

    private AuthResponse setCookie(AuthResponse authResponse, HttpServletResponse response){
        ResponseCookie cookie = ResponseCookie.from("accessToken", authResponse.getAccessToken())
                .httpOnly(true)
                .path("/")
                .maxAge(tokenAge)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
        return authResponse;
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest, HttpServletResponse response){
        AuthResponse authResponse = authService.login(authRequest);
        return ResponseEntity.ok(setCookie(authResponse, response));
    }

}
