package com.ledgerly.dto;

import com.ledgerly.entity.ExpenseCategory;
import java.math.BigDecimal;

public record CategorySummary(ExpenseCategory category, BigDecimal total) {
}