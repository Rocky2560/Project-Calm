package com.example.Project.Calm.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {
    @Email @NotBlank
    private String email;

    @NotBlank @Size(min = 6)
    private String password;

    private String fullName;
}
