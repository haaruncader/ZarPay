package com.zarpay.controller;

import com.zarpay.entity.User;
import com.zarpay.entity.Wallet;
import com.zarpay.service.TransactionService;
import com.zarpay.service.UserService;
import com.zarpay.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/deposit")
@RequiredArgsConstructor
public class DepositController {

    private final UserService userService;
    private final WalletService walletService;
    private final TransactionService transactionService;

    @PostMapping
    public String deposit(
            @RequestParam String email,
            @RequestParam BigDecimal amount
    ) {
        User user = userService.getByEmail(email);
        Wallet wallet = walletService.getWallet(user);
        transactionService.deposit(wallet, amount);
        return "Deposit successful";
    }

}
