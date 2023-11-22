package com.example.authservice.services;

import com.example.authservice.entities.AuthRequest;
import com.example.authservice.entities.AuthResponse;
import com.example.authservice.entities.User;
import org.apache.sshd.common.config.keys.loader.openssh.kdf.BCrypt;
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

    @Autowired
    public AuthService(RestTemplate restTemplate,
                       JwtUtil jwtUtil,
                       @Value("${service.user-service.get-user}") String getUserLink,
                       @Value("${service.user-service.post-user}") String postUserLink,
                       @Value("${service.user-service.is-exist}") String isExistUserLink) {
        this.restTemplate = restTemplate;
        this.jwtUtil = jwtUtil;
        this.getUserLink = getUserLink;
        this.postUserLink = postUserLink;
        this.isExistUserLink = isExistUserLink;
    }

    public AuthResponse register(AuthRequest request) {

        if(isExistUser(request.getLogin())) {
            throw new RuntimeException("this user already in database");
        }
        request.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        User registeredUser = restTemplate.postForObject(postUserLink, request, User.class);

        String accessToken = jwtUtil.generate(registeredUser.getLogin(), registeredUser.getRole(), "ACCESS");
        String refreshToken = jwtUtil.generate(registeredUser.getLogin(), registeredUser.getRole(), "REFRESH");

        return new AuthResponse(accessToken, refreshToken);

    }

    private boolean isExistUser(String login){
        return Boolean.TRUE.equals(restTemplate.postForObject(postUserLink, login, Boolean.class));
    }


    public AuthResponse login(AuthRequest authRequest) {
        return null;
    }


}
