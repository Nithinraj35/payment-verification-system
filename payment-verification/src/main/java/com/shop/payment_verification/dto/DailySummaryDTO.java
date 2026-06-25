package com.shop.payment_verification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data @AllArgsConstructor
public class DailySummaryDTO {
    private String date;
    private BigDecimal totalAmount;
    private Long successCount;
    private Long pendingCount;
    private Long failedCount;
}