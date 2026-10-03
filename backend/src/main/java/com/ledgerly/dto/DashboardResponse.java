package com.ledgerly.dto;

import java.math.BigDecimal;
import java.util.List;

public record DashboardResponse(BigDecimal monthlyExpenseTotal, long activeSubscriptionCount,
                                BigDecimal monthlySubscriptionCost, BigDecimal yearlySubscriptionCost,
                                List<SubscriptionResponse> upcomingSubscriptions,
                                List<CategorySummary> categorySummary) {
}