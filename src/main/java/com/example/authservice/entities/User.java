package com.example.authservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private String login;
    private String password;
    private String role;
    private String farmerId;
}
