package com.lydiasystems.challenge.repository;

import com.lydiasystems.challenge.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
