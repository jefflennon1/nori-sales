package com.noriservices.norisales.domain.product;

import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, UUID> {

    Optional<ProductModel> findById(@NonNull UUID id);

    Optional<ProductModel> findByNameAndPrice(String name, BigDecimal price);
}
