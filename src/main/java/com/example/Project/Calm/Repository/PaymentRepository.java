package com.example.Project.Calm.Repository;

import com.example.Project.Calm.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    List<Payment> findByUserId(String userId);
    List<Payment> findByUserIdAndStatus(String userId, String status);
    List<Payment> findByClientId(String clientId);
}
