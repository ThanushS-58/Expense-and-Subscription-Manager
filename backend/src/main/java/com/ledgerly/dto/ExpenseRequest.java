package com.ledgerly.dto;

import com.ledgerly.entity.ExpenseCategory;
import com.ledgerly.entity.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseRequest(
        @NotNull @Positive BigDecimal amount,
        @NotNull ExpenseCategory category,
        @Size(max = 255) String description,
        @NotNull PaymentMethod paymentMethod,
        @NotNull LocalDate expenseDate) {
}