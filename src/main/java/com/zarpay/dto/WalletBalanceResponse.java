package com.zarpay.dto;

import java.math.BigDecimal;

public record WalletBalanceResponse(
        String email,
        BigDecimal balance
) {}
