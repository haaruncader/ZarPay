package com.zarpay.controller;

import com.zarpay.dto.WalletBalanceResponse;
import com.zarpay.dto.TransactionResponse;
import com.zarpay.entity.Transaction;
import com.zarpay.entity.User;
import com.zarpay.entity.Wallet;
import com.zarpay.service.TransactionService;
import com.zarpay.service.UserService;
import com.zarpay.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final UserService userService;
    private final WalletService walletService;
    private final TransactionService transactionService;

    /**
     * Get wallet balance
     * Example:
     * GET /api/wallet/balance?email=test@example.com
     */
    @GetMapping("/balance")
    public WalletBalanceResponse  getBalance(@RequestParam String email) {
        User user = userService.getByEmail(email);
        Wallet wallet = walletService.getWallet(user);

        BigDecimal balance = transactionService.getBalance(wallet);

        return new WalletBalanceResponse(
                user.getEmail(),
                balance
        );
    }

    /**
     * Transaction history
     */
    @GetMapping("/transactions")
    public List<TransactionResponse> getTransactions(@RequestParam String email) {
        User user = userService.getByEmail(email);
        Wallet wallet = walletService.getWallet(user);

        List<Transaction> transactions = transactionService.getWalletTransactions(wallet);
        List<TransactionResponse> responses = new ArrayList<>(transactions.size());

        for (Transaction tx : transactions) {
            String fromEmail = tx.getFromWallet() != null
                    ? tx.getFromWallet().getUser().getEmail()
                    : null;
            String toEmail = tx.getToWallet() != null
                    ? tx.getToWallet().getUser().getEmail()
                    : null;

            responses.add(new TransactionResponse(
                    tx.getType().name(),
                    tx.getAmount(),
                    fromEmail,
                    toEmail,
                    tx.getCreatedAt(),
                    tx.getBalanceAfter()
            ));
        }
        return responses;
    }
}

