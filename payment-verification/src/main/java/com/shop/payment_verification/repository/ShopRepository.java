package com.shop.payment_verification.repository;

import com.shop.payment_verification.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    Optional<Shop> findByOwner_UserId(Long ownerId);
}