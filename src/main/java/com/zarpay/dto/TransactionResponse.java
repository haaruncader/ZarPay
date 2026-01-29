package com.zarpay.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionResponse(
        String type,
        BigDecimal amount,
        String from,
        String to,
        Instant timestamp
) {}
