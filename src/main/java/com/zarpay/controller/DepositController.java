package com.zarpay.controller;

import com.zarpay.dto.DepositRequest;
import com.zarpay.entity.User;
import com.zarpay.entity.Wallet;
import com.zarpay.service.TransactionService;
import com.zarpay.service.UserService;
import com.zarpay.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/deposit")
@RequiredArgsConstructor
public class DepositController {

    private final UserService userService;
    private final WalletService walletService;
    private final TransactionService transactionService;

    @PostMapping
    public String deposit(@RequestBody @Valid DepositRequest request) {

        User user = userService.getByEmail(request.getEmail());
        Wallet wallet = walletService.getWallet(user);
        transactionService.deposit(wallet, request.getAmount());

        return "Deposit successful";
    }
}
