package com.shop.payment_verification.repository;

import com.shop.payment_verification.model.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WorkerRepository extends JpaRepository<Worker, Long> {
    List<Worker> findByShop_ShopId(Long shopId);
    boolean existsByUser_UserId(Long userId);
}