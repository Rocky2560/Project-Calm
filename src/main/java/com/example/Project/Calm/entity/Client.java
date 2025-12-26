// src/main/java/com/example/calm/model/Client.java
package com.example.Project.Calm.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "clients")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    private String email;
    private String phone;
    private String utr;
    private String companyNumber;

    @Column(nullable = false)
    private String status = "active";

    @ElementCollection
    @CollectionTable(name = "client_services")
    private List<String> services;

    private String paymentStatus;
    private String loeStatus;
    private Boolean photoIdVerified = false;
    private Boolean auth648Submitted = false;
    private Boolean amlccVerified = false;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
}

