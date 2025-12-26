//package com.example.Project.Calm.DTO;
//
//// src/main/java/com/example/calm/dto/NewClientDto.java
//
//import com.example.Project.Calm.entity.ClientServiceType;
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotBlank;
//import lombok.Data;
//
//import java.util.HashSet;
//import java.util.Set;
//
//@Data
//public class NewClientDto {
//
//    @NotBlank(message = "Name is required")
//    private String name;
//
//    @NotBlank(message = "Email is required")
//    @Email(message = "Invalid email format")
//    private String email;
//
//    private String phone;
//    private String address;
//
//    private String utr;
//    private String companyRegNo;
//    private String vatNo;
//    private String payrollNo;
//
//    private Set<ClientServiceType> services = new HashSet<>();
//    private Set<String> benefits = new HashSet<>();
//    private String benefitNotes;
//    private String internalNotes;
//
//    // Getters & Setters
//}
