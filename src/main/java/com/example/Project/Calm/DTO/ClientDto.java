package com.example.Project.Calm.DTO;

// src/main/java/com/example/calm/dto/ClientDto.java

import com.example.Project.Calm.model.ClientServiceType;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class ClientDto {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String utr;
    private String companyRegNo;
    private String vatNo;
    private String payrollNo;
    private Set<ClientServiceType> services = new HashSet<>();
    private Set<String> benefits = new HashSet<>();
    private String benefitNotes;
    private String internalNotes;
    private LocalDate createdAt;
    private Integer pendingPayments;
    private Integer overduePayments;
    private Integer paidThisYear;

    // Getters & Setters
}
