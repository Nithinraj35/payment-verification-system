package com.shop.payment_verification.service;

import com.shop.payment_verification.config.JwtUtil;
import com.shop.payment_verification.dto.*;
import com.shop.payment_verification.model.*;
import com.shop.payment_verification.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final ShopRepository shopRepo;
    private final WorkerRepository workerRepo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    @Transactional
    public String register(RegisterRequest req) {
        if (userRepo.existsByEmail(req.getEmail()))
            throw new RuntimeException("Email already registered");

        User.Role role = User.Role.valueOf(req.getRole().toUpperCase());
        Shop shop = null;

        if (role == User.Role.OWNER) {
            shop = shopRepo.save(Shop.builder().shopName(req.getShopName()).build());
        } else {
            shop = shopRepo.findById(req.getShopId())
                    .orElseThrow(() -> new RuntimeException("Shop not found"));
        }

        User user = User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .password(encoder.encode(req.getPassword()))
                .role(role)
                .shop(shop)
                .build();
        userRepo.save(user);

        if (role == User.Role.OWNER) {
            shop.setOwner(user);
            shopRepo.save(shop);
        } else {
            Worker worker = Worker.builder()
                    .user(user)
                    .shop(shop)
                    .workerName(req.getName())
                    .mobile(req.getMobile())
                    .build();
            workerRepo.save(worker);
        }
        return "Registration successful";
    }

    public JwtResponse login(LoginRequest req) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        User user = userRepo.findByEmail(req.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        Long shopId = user.getShop() != null ? user.getShop().getShopId() : null;
        return new JwtResponse(token, user.getRole().name(), user.getName(), shopId);
    }
}