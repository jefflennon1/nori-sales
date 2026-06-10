package com.noriservices.norisales.domain.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentModel, UUID> {

    Optional<PaymentModel> findByOrderIdAndStatus(UUID orderId, PaymentStatus status);
}
