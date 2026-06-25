package com.shop.payment_verification.service;

import com.shop.payment_verification.dto.PaymentDTO;
import com.shop.payment_verification.exception.ResourceNotFoundException;
import com.shop.payment_verification.model.*;
import com.shop.payment_verification.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepo;
    private final UserRepository userRepo;
    private final ShopRepository shopRepo;

    public PaymentDTO findByTransactionId(String txnId) {
        return toDTO(paymentRepo.findByTransactionId(txnId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found: " + txnId)));
    }

    public PaymentDTO findByUpiRef(String upiRef) {
        return toDTO(paymentRepo.findByUpiRefNumber(upiRef)
                .orElseThrow(() -> new ResourceNotFoundException("UPI Ref not found: " + upiRef)));
    }

    public List<PaymentDTO> findByMobile(String mobile, Long shopId) {
        return paymentRepo.findByCustomerMobileAndShop_ShopId(mobile, shopId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<PaymentDTO> getRecentPayments(Long shopId) {
        return paymentRepo.findByShop_ShopIdOrderByPaymentTimeDesc(shopId)
                .stream().limit(20).map(this::toDTO).collect(Collectors.toList());
    }

    public PaymentDTO updateStatus(Long paymentId, String status, String verifierEmail) {
        Payment p = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
        p.setPaymentStatus(Payment.PaymentStatus.valueOf(status.toUpperCase()));
        User verifier = userRepo.findByEmail(verifierEmail).orElse(null);
        p.setVerifiedBy(verifier);
        return toDTO(paymentRepo.save(p));
    }

    public PaymentDTO addPayment(PaymentDTO dto) {
        Shop shop = shopRepo.findById(dto.getShopId())
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found"));
        Payment p = Payment.builder()
                .shop(shop)
                .transactionId(dto.getTransactionId())
                .upiRefNumber(dto.getUpiRefNumber())
                .customerName(dto.getCustomerName())
                .customerMobile(dto.getCustomerMobile())
                .amount(dto.getAmount())
                .paymentStatus(Payment.PaymentStatus.PENDING)
                .notes(dto.getNotes())
                .build();
        return toDTO(paymentRepo.save(p));
    }

    private PaymentDTO toDTO(Payment p) {
        PaymentDTO dto = new PaymentDTO();
        dto.setPaymentId(p.getPaymentId());
        dto.setTransactionId(p.getTransactionId());
        dto.setUpiRefNumber(p.getUpiRefNumber());
        dto.setCustomerName(p.getCustomerName());
        dto.setCustomerMobile(p.getCustomerMobile());
        dto.setAmount(p.getAmount());
        dto.setPaymentStatus(p.getPaymentStatus());
        dto.setPaymentTime(p.getPaymentTime());
        dto.setNotes(p.getNotes());
        if (p.getVerifiedBy() != null)
            dto.setVerifiedByName(p.getVerifiedBy().getName());
        return dto;
    }
}