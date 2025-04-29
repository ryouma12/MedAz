package com.example.medaz.dto.auth;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String identificationNumber;
    private String email;
    private String firstName;
    private String lastName;
    private List<String> roles;

    public JwtResponse(String accessToken, Long id, String identificationNumber, String email, String firstName, String lastName, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.identificationNumber = identificationNumber;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
    }
}
