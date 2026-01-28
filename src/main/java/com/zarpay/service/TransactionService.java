package com.zarpay.service;

import com.zarpay.entity.*;
import com.zarpay.repository.TransactionRepository;
import com.zarpay.repository.WalletRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    /**
     * Read-only derived balance
     */
    @Transactional
    public BigDecimal getBalance(Wallet wallet) {
        BigDecimal incoming = transactionRepository.sumIncoming(wallet);
        BigDecimal outgoing = transactionRepository.sumOutgoing(wallet);
        return incoming.subtract(outgoing);
    }

    /**
     * Internal deposit (Stripe webhook will call this later)
     */
    @Transactional
    public Transaction deposit(Wallet toWallet, BigDecimal amount) {
        validateAmount(amount);

        Transaction tx = Transaction.builder()
                .toWallet(toWallet)
                .amount(amount)
                .type(TransactionType.DEPOSIT)
                .status(TransactionStatus.COMPLETED)
                .build();

        return transactionRepository.save(tx);
    }

    /**
     * Wallet → Wallet transfer
     */
    @Transactional
    public Transaction transfer(Wallet fromWallet, Wallet toWallet, BigDecimal amount) {
        validateAmount(amount);

        BigDecimal senderBalance = getBalance(fromWallet);

        if (senderBalance.compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient balance");
        }

        Transaction tx = Transaction.builder()
                .fromWallet(fromWallet)
                .toWallet(toWallet)
                .amount(amount)
                .type(TransactionType.TRANSFER)
                .status(TransactionStatus.COMPLETED)
                .build();

        return transactionRepository.save(tx);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }

    public List<Transaction> getWalletTransactions(Wallet wallet) {
        return transactionRepository.findByFromWalletOrToWallet(wallet, wallet);
    }

}

