package com.zarpay.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;


@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Wallet money is coming FROM (null for deposits)
     */
    @ManyToOne
    @JoinColumn(name = "from_wallet_id")
    private Wallet fromWallet;

    /**
     * Wallet money is going TO (null for withdrawals)
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "to_wallet_id")
    private Wallet toWallet;

    /**
     * Always POSITIVE.
     * Direction is implied by from/to wallets.
     */
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    /**
     * DEPOSIT, TRANSFER
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    /**
     * COMPLETED, FAILED, PENDING
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }
}
