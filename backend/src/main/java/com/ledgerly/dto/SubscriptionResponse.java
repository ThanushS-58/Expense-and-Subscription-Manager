package com.ledgerly.dto;

import com.ledgerly.entity.BillingCycle;
import com.ledgerly.entity.SubscriptionStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

public record SubscriptionResponse(Long id, String name, BigDecimal amount, BillingCycle billingCycle,
                                   String category, LocalDate nextBillingDate, SubscriptionStatus status,
                                   Long userId) {
}