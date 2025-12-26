package com.example.Project.Calm.DTO;

// src/main/java/com/example/calm/dto/ClientDto.java


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

@Data
public class ClientDTO {
    private String id;

    @NotBlank
    private String name;

    private String email;
    private String phone;
    private String utr;
    private String companyNumber;
    private String status;
    private List<String> services;
    private String paymentStatus;
    private String loeStatus;
    private Boolean photoIdVerified;
    private Boolean auth648Submitted;
    private Boolean amlccVerified;
    private String notes;
}