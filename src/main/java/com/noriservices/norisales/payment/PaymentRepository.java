package com.noriservices.norisales.payment;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    Optional<Payment> findByOrderIdAndStatus(UUID orderId, PaymentStatus status);

    Optional<Payment> findByExternalId(String id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
       select payment
       from Payment payment
       where payment.externalId = :externalId
       """)
    Optional<Payment> findByExternalIdForUpdate(
            @Param("externalId") String externalId
    );
}
