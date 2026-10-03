package com.ledgerly.dto;

import com.ledgerly.entity.BillingCycle;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public record SubscriptionRequest(
        @NotBlank @Size(max = 100) String name,
        @NotNull @Positive BigDecimal amount,
        @NotNull BillingCycle billingCycle,
        @NotBlank @Size(max = 50) String category,
        @NotNull LocalDate nextBillingDate) {
}