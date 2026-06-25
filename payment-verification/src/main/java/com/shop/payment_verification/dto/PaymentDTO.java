package com.shop.payment_verification.dto;

import com.shop.payment_verification.model.Payment.PaymentStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentDTO {
    private Long paymentId;
    private String transactionId;
    private String upiRefNumber;
    private String customerName;
    private String customerMobile;
    private BigDecimal amount;
    private PaymentStatus paymentStatus;
    private LocalDateTime paymentTime;
    private String verifiedByName;
    private String notes;
    private Long shopId;
    private String newStatus;
}