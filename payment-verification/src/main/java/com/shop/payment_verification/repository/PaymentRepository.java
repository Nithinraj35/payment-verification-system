package com.shop.payment_verification.repository;

import com.shop.payment_verification.model.Payment;
import com.shop.payment_verification.model.Payment.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByTransactionId(String transactionId);
    Optional<Payment> findByUpiRefNumber(String upiRefNumber);

    List<Payment> findByCustomerMobileAndShop_ShopId(String mobile, Long shopId);
    List<Payment> findByShop_ShopIdOrderByPaymentTimeDesc(Long shopId);

    List<Payment> findByShop_ShopIdAndPaymentTimeBetweenOrderByPaymentTimeDesc(
            Long shopId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p " +
            "WHERE p.shop.shopId = :shopId AND p.paymentStatus = 'SUCCESS' " +
            "AND p.paymentTime >= :start AND p.paymentTime < :end")
    BigDecimal sumSuccessfulAmountByShopAndDate(
            @Param("shopId") Long shopId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(p) FROM Payment p " +
            "WHERE p.shop.shopId = :shopId " +
            "AND p.paymentTime >= :start AND p.paymentTime < :end " +
            "AND p.paymentStatus = :status")
    Long countByShopAndDateAndStatus(
            @Param("shopId") Long shopId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("status") PaymentStatus status);
}