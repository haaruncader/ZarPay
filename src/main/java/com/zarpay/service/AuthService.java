package com.zarpay.service;

import com.zarpay.dto.SignupRequest;
import com.zarpay.dto.SignupResponse;
import com.zarpay.entity.User;
import com.zarpay.entity.Wallet;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final WalletService walletService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public SignupResponse signup(SignupRequest request) {

        User user = User.builder()
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .active(true)
                .build();

        User savedUser = userService.save(user);

        Wallet wallet = walletService.createWallet(savedUser);

        return new SignupResponse(savedUser.getEmail(), wallet.getId().toString());
    }
}
