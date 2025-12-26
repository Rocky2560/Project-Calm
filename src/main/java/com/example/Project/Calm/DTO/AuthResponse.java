package com.example.Project.Calm.DTO;

import lombok.*;

@Data @AllArgsConstructor
public class AuthResponse {
    private String token;
    private String userId;
    private String email;
    private String fullName;
}
