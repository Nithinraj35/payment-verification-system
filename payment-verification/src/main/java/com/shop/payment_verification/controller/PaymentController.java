package com.shop.payment_verification.controller;

import com.shop.payment_verification.dto.PaymentDTO;
import com.shop.payment_verification.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/search/txn/{txnId}")
    public ResponseEntity<PaymentDTO> byTxn(@PathVariable String txnId) {
        return ResponseEntity.ok(paymentService.findByTransactionId(txnId));
    }

    @GetMapping("/search/upi/{upiRef}")
    public ResponseEntity<PaymentDTO> byUpi(@PathVariable String upiRef) {
        return ResponseEntity.ok(paymentService.findByUpiRef(upiRef));
    }

    @GetMapping("/search/mobile/{mobile}")
    public ResponseEntity<List<PaymentDTO>> byMobile(
            @PathVariable String mobile,
            @RequestParam Long shopId) {
        return ResponseEntity.ok(paymentService.findByMobile(mobile, shopId));
    }

    @GetMapping("/recent")
    public ResponseEntity<List<PaymentDTO>> recent(@RequestParam Long shopId) {
        return ResponseEntity.ok(paymentService.getRecentPayments(shopId));
    }

    @PostMapping
    public ResponseEntity<PaymentDTO> add(@RequestBody PaymentDTO dto) {
        return ResponseEntity.ok(paymentService.addPayment(dto));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<PaymentDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            Authentication auth) {
        return ResponseEntity.ok(
                paymentService.updateStatus(id, status, auth.getName()));
    }
}