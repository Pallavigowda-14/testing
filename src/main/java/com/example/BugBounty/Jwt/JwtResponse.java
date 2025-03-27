package com.example.BugBounty.Jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String fullName;
    private String email;
    private Set<String> roles;

    public JwtResponse(String token, Long id, String fullName, String email, Set<String> roles) {
        this.token = token;
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.roles = roles;
    }
}