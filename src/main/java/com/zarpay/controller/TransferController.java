package com.zarpay.controller;

import com.zarpay.entity.User;
import com.zarpay.entity.Wallet;
import com.zarpay.service.TransactionService;
import com.zarpay.service.UserService;
import com.zarpay.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

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
    public String transfer(
            @RequestParam String fromEmail,
            @RequestParam String toEmail,
            @RequestParam BigDecimal amount
    ) {
        User fromUser = userService.getByEmail(fromEmail);
        User toUser = userService.getByEmail(toEmail);

        Wallet fromWallet = walletService.getWallet(fromUser);
        Wallet toWallet = walletService.getWallet(toUser);

        transactionService.transfer(fromWallet, toWallet, amount);

        return "Transfer successful";
    }
}
