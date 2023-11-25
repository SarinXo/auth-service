package com.example.authservice.services;

import com.example.authservice.dto.AuthRequest;
import com.example.authservice.dto.AuthResponse;
import com.example.authservice.dto.User;
import com.example.authservice.handlers.exceptions.AuthError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {

    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;
    private final String getUserLink;
    private final String postUserLink;
    private final String isExistUserLink;
    private final String isCorrectUserLink;

    @Autowired
    public AuthService(RestTemplate restTemplate,
                       JwtUtil jwtUtil,
                       @Value("${service.user-service.get-user}") String getUserLink,
                       @Value("${service.user-service.post-user}") String postUserLink,
                       @Value("${service.user-service.is-exist}") String isExistUserLink,
                       @Value("${service.user-service.is-correct}") String isCorrectUserLink) {
        this.restTemplate = restTemplate;
        this.jwtUtil = jwtUtil;
        this.getUserLink = getUserLink;
        this.postUserLink = postUserLink;
        this.isExistUserLink = isExistUserLink;
        this.isCorrectUserLink = isCorrectUserLink;
    }

    public AuthResponse register(AuthRequest request) {
        if(isExistUser(request.getLogin())) {
            throw new AuthError("this user already in database");
        }
        User user = restTemplate.postForObject(postUserLink, request, User.class);
        return generateToken(user, request);
    }

    private boolean isExistUser(String login){
        return Boolean.TRUE.equals(restTemplate.getForObject(isExistUserLink + "/" + login, Boolean.class));
    }

    public AuthResponse login(AuthRequest request) {
        if(!isCorrectData(request)){
            throw new AuthError("wrong login or password");
        }
        User user = restTemplate.getForObject(getUserLink + "/" + request.getLogin(), User.class);
        return generateToken(user, request);
    }

    private boolean isCorrectData(AuthRequest request){
        return Boolean.TRUE.equals(restTemplate.postForObject(isCorrectUserLink, request, Boolean.class));
    }

    private AuthResponse generateToken(User user, AuthRequest request){

        String accessToken = jwtUtil.generate( user.getLogin(), user.getRole(), "ACCESS");
        String refreshToken = jwtUtil.generate(user.getLogin(), user.getRole(), "REFRESH");

        return new AuthResponse(accessToken, refreshToken);
    }

}
