package com.shop.payment_verification.service;

import com.shop.payment_verification.dto.DailySummaryDTO;
import com.shop.payment_verification.model.Payment.PaymentStatus;
import com.shop.payment_verification.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service @RequiredArgsConstructor
public class DashboardService {

    private final PaymentRepository paymentRepo;

    public DailySummaryDTO getDailySummary(Long shopId, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end   = date.plusDays(1).atStartOfDay();

        BigDecimal total = paymentRepo.sumSuccessfulAmountByShopAndDate(shopId, start, end);
        Long success = paymentRepo.countByShopAndDateAndStatus(shopId, start, end, PaymentStatus.SUCCESS);
        Long pending = paymentRepo.countByShopAndDateAndStatus(shopId, start, end, PaymentStatus.PENDING);
        Long failed  = paymentRepo.countByShopAndDateAndStatus(shopId, start, end, PaymentStatus.FAILED);

        return new DailySummaryDTO(date.toString(),
                total != null ? total : BigDecimal.ZERO,
                success, pending, failed);
    }
}