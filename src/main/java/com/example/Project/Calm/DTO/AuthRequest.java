package com.example.Project.Calm.DTO;

// src/main/java/com/calm/dto/AuthRequest.java

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AuthRequest {
    @Email @NotBlank
    private String email;

    @NotBlank @Size(min = 6)
    private String password;
}


