package com.example.Project.Calm.DTO;


import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PaymentDTO {
    private String id;

    @NotNull @Positive
    private BigDecimal amount;

    private String currency;
    private String status;
    private String method;
    private String description;
    private LocalDate dueDate;
    private LocalDate paidDate;
    private String clientId;
    private String clientName;
}

