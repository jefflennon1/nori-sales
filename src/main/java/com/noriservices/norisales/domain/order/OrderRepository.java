package com.noriservices.norisales.domain.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<OrderModel, UUID> {

    List<OrderModel> findByUserId(UUID userId);

    List<OrderModel> findByUserIdAndStatus(UUID userId, OrderStatus status);

    List<OrderModel> findByUserIdOrderByCreatedAtDesc(UUID userId);

    boolean existsByUserIdAndStatus(UUID userId, OrderStatus status);

    Page<OrderModel> findByUserId(UUID userId, Pageable pageable);
}
