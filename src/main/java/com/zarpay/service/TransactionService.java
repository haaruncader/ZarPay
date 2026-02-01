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

        BigDecimal currentBalance = getBalance(toWallet);
        BigDecimal balanceAfter = currentBalance.add(amount);

        Transaction tx = Transaction.builder()
                .toWallet(toWallet)
                .amount(amount)
                .type(TransactionType.CREDIT)
                .status(TransactionStatus.COMPLETED)
                .balanceAfter(balanceAfter)
                .build();

        return transactionRepository.save(tx);
    }

    /**
     * Wallet → Wallet transfer
     */
    @Transactional
    public void transfer(Wallet fromWallet, Wallet toWallet, BigDecimal amount) {
        validateAmount(amount);

        BigDecimal senderBalance = getBalance(fromWallet);

        if (senderBalance.compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient balance");
        }

        // Sender's balance after transfer
        BigDecimal senderBalanceAfter = senderBalance.subtract(amount);

        // Receiver's balance after transfer
        BigDecimal receiverBalance = getBalance(toWallet);
        BigDecimal receiverBalanceAfter = receiverBalance.add(amount);

        // DEBIT (Sender loses money)
        Transaction debit = Transaction.builder()
                .fromWallet(fromWallet)
                .toWallet(toWallet)
                .amount(amount)
                .type(TransactionType.DEBIT)
                .status(TransactionStatus.COMPLETED)
                .balanceAfter(senderBalanceAfter)
                .build();
            transactionRepository.save(debit);


        // CREDIT (Receiver gains money)
        Transaction credit = Transaction.builder()
                .fromWallet(fromWallet)
                .toWallet(toWallet)
                .amount(amount)
                .type(TransactionType.CREDIT)
                .status(TransactionStatus.COMPLETED)
                .balanceAfter(receiverBalanceAfter)
                .build();
        transactionRepository.save(credit);

    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }

    public List<Transaction> getWalletTransactions(Wallet wallet) {
        return transactionRepository.findTransactionsForWallet(wallet);
    }

}

