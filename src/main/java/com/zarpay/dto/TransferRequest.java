package com.zarpay.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransferRequest {

    @NotBlank(message = "From email is required")
    @Email(message = "From email must be valid")
    private String fromEmail;

    @NotBlank(message = "To email is required")
    @Email(message = "To email must be valid")
    private String toEmail;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be positive")
    private BigDecimal amount;
}
