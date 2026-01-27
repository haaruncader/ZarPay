package com.zarpay.controller;

import com.zarpay.entity.Transaction;
import com.zarpay.entity.User;
import com.zarpay.entity.Wallet;
import com.zarpay.repository.TransactionRepository;
import com.zarpay.service.TransactionService;
import com.zarpay.service.UserService;
import com.zarpay.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final UserService userService;
    private final WalletService walletService;
    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;

    /**
     * Get wallet balance
     * Example:
     * GET /api/wallet/balance?email=test@example.com
     */
    @GetMapping("/balance")
    public BigDecimal getBalance(@RequestParam String email) {
        User user = userService.getByEmail(email);
        Wallet wallet = walletService.getWallet(user);
        return transactionService.getBalance(wallet);
    }

    /**
     * Transaction history
     */
    @GetMapping("/transactions")
    public List<Transaction> getTransactions(@RequestParam String email) {
        User user = userService.getByEmail(email);
        Wallet wallet = walletService.getWallet(user);
        return transactionRepository.findByFromWalletOrToWallet(wallet, wallet);
    }


}

