package com.example.authservice.entities;

import lombok.Data;

@Data
public class User {
    private String id;
    private String login;
    private String password;
    private String role;
}
