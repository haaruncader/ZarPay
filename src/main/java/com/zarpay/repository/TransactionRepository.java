package com.zarpay.repository;

import com.zarpay.entity.Transaction;
import com.zarpay.entity.Wallet;
import com.zarpay.entity.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    /**
     * All transactions involving a wallet
     * (sent OR received)
     */
    List<Transaction> findByFromWalletOrToWallet(
            Wallet fromWallet,
            Wallet toWallet
    );

    /**
     * Useful for admin / reconciliation
     */
    List<Transaction> findByStatus(TransactionStatus status);

    @Query("""
        SELECT COALESCE(SUM(t.amount), 0)
        FROM Transaction t
        WHERE t.toWallet = :wallet
        AND t.status = 'COMPLETED'
    """)
    BigDecimal sumIncoming(Wallet wallet);

    @Query("""
        SELECT COALESCE(SUM(t.amount), 0)
        FROM Transaction t
        WHERE t.fromWallet = :wallet
        AND t.status = 'COMPLETED'
    """)
    BigDecimal sumOutgoing(Wallet wallet);
}

