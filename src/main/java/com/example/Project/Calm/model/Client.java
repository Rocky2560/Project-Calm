// src/main/java/com/example/calm/model/Client.java
package com.example.Project.Calm.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "clients")
@Data
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;
    private String address;
    private String utr;
    private String companyRegNo;
    private String vatNo;
    private String payrollNo;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "client_services", joinColumns = @JoinColumn(name = "client_id"))
    @Enumerated(EnumType.STRING)
    private Set<ClientServiceType> services = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "client_benefits", joinColumns = @JoinColumn(name = "client_id"))
    private Set<String> benefits = new HashSet<>();

    private String benefitNotes;
    private String internalNotes;
    private LocalDate createdAt = LocalDate.now();
    private LocalDate lastContact;

    private Integer pendingPayments = 0;
    private Integer overduePayments = 0;
    private Integer paidThisYear = 0;

    public Client() {}

    // Getters & Setters
    // (Omitted for brevity but should be included in your project)
}
