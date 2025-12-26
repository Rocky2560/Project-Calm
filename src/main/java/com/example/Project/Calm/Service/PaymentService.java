package com.example.Project.Calm.Service;


import com.example.Project.Calm.DTO.PaymentDTO;
import com.example.Project.Calm.entity.*;
import com.example.Project.Calm.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepo;
    private final ClientRepository clientRepo;
    private final UserRepository userRepo;

    public List<PaymentDTO> getAllPayments(String userId) {
        return paymentRepo.findByUserId(userId).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public List<PaymentDTO> getPaymentsByStatus(String userId, String status) {
        return paymentRepo.findByUserIdAndStatus(userId, status).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public PaymentDTO createPayment(PaymentDTO dto, String userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Payment payment = Payment.builder()
                .amount(dto.getAmount())
                .currency(dto.getCurrency() != null ? dto.getCurrency() : "GBP")
                .status(dto.getStatus() != null ? dto.getStatus() : "pending")
                .method(dto.getMethod())
                .description(dto.getDescription())
                .dueDate(dto.getDueDate())
                .paidDate(dto.getPaidDate())
                .user(user)
                .build();

        if (dto.getClientId() != null) {
            Client client = clientRepo.findById(dto.getClientId())
                    .orElseThrow(() -> new RuntimeException("Client not found"));
            payment.setClient(client);
        }

        return toDTO(paymentRepo.save(payment));
    }

    public PaymentDTO updatePayment(String id, PaymentDTO dto, String userId) {
        Payment payment = paymentRepo.findById(id)
                .filter(p -> p.getUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setAmount(dto.getAmount());
        payment.setStatus(dto.getStatus());
        payment.setMethod(dto.getMethod());
        payment.setDescription(dto.getDescription());
        payment.setDueDate(dto.getDueDate());
        payment.setPaidDate(dto.getPaidDate());
        payment.setUpdatedAt(LocalDateTime.now());

        return toDTO(paymentRepo.save(payment));
    }

    public void deletePayment(String id, String userId) {
        Payment payment = paymentRepo.findById(id)
                .filter(p -> p.getUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        paymentRepo.delete(payment);
    }

    private PaymentDTO toDTO(Payment p) {
        PaymentDTO dto = new PaymentDTO();
        dto.setId(p.getId());
        dto.setAmount(p.getAmount());
        dto.setCurrency(p.getCurrency());
        dto.setStatus(p.getStatus());
        dto.setMethod(p.getMethod());
        dto.setDescription(p.getDescription());
        dto.setDueDate(p.getDueDate());
        dto.setPaidDate(p.getPaidDate());
        if (p.getClient() != null) {
            dto.setClientId(p.getClient().getId());
            dto.setClientName(p.getClient().getName());
        }
        return dto;
    }
}