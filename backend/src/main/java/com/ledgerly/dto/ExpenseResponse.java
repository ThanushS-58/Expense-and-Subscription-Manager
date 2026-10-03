package com.ledgerly.dto;

import com.ledgerly.entity.ExpenseCategory;
import com.ledgerly.entity.PaymentMethod;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseResponse(Long id, BigDecimal amount, ExpenseCategory category, String description,
                              PaymentMethod paymentMethod, LocalDate expenseDate, Long userId) {
}