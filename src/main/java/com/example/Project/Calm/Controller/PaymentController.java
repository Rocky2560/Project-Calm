package com.example.Project.Calm.Controller;


import com.example.Project.Calm.DTO.PaymentDTO;
import com.example.Project.Calm.Service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping
    public ResponseEntity<List<PaymentDTO>> getAll(
            @RequestParam(required = false) String status, Authentication auth) {
        if (status != null) {
            return ResponseEntity.ok(paymentService.getPaymentsByStatus(auth.getName(), status));
        }
        return ResponseEntity.ok(paymentService.getAllPayments(auth.getName()));
    }

    @PostMapping
    public ResponseEntity<PaymentDTO> create(
            @Valid @RequestBody PaymentDTO dto, Authentication auth) {
        return ResponseEntity.ok(paymentService.createPayment(dto, auth.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentDTO> update(
            @PathVariable String id, @Valid @RequestBody PaymentDTO dto, Authentication auth) {
        return ResponseEntity.ok(paymentService.updatePayment(id, dto, auth.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id, Authentication auth) {
        paymentService.deletePayment(id, auth.getName());
        return ResponseEntity.noContent().build();
    }
}

