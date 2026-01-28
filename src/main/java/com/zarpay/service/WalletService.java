package com.zarpay.service;

import com.zarpay.entity.User;
import com.zarpay.entity.Wallet;
import com.zarpay.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    public Wallet getWallet(User user) {
        return walletRepository.findByUser(user)
                .orElseThrow(() -> new IllegalStateException("Wallet not found"));
    }

    public Wallet createWallet(User user) {
        return walletRepository.save(Wallet.builder().user(user).currency("ZAR").build());
    }
}
