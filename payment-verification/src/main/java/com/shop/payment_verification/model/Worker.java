package com.shop.payment_verification.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "workers")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Worker {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workerId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @Column(nullable = false)
    private String workerName;

    private String mobile;

    private LocalDateTime joinedAt = LocalDateTime.now();
}