package com.zarpay.controller;

import com.zarpay.dto.TransferRequest;
import com.zarpay.entity.User;
import com.zarpay.entity.Wallet;
import com.zarpay.service.TransactionService;
import com.zarpay.service.UserService;
import com.zarpay.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/transfer")
@RequiredArgsConstructor
public class TransferController {

    private final UserService userService;
    private final WalletService walletService;
    private final TransactionService transactionService;

    /**
     * Send money from one user to another
     */
    @PostMapping
    public String transfer(@RequestBody @Valid TransferRequest request) {

        if (request.getFromEmail().equalsIgnoreCase(request.getToEmail())) {
            throw new IllegalArgumentException("Cannot transfer to yourself");
        }

        User fromUser = userService.getByEmail(request.getFromEmail());
        User toUser = userService.getByEmail(request.getToEmail());

        Wallet fromWallet = walletService.getWallet(fromUser);
        Wallet toWallet = walletService.getWallet(toUser);

        transactionService.transfer(fromWallet, toWallet, request.getAmount());

        return "Transfer successful";
    }
}
