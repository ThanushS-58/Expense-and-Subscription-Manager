package com.ledgerly.service;

import com.ledgerly.dto.CategorySummary;
import com.ledgerly.dto.DashboardResponse;
import com.ledgerly.entity.ExpenseCategory;
import com.ledgerly.entity.Subscription;
import com.ledgerly.repository.ExpenseRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardService {
    private final ExpenseRepository expenseRepository;
    private final SubscriptionService subscriptionService;

    public DashboardService(ExpenseRepository expenseRepository, SubscriptionService subscriptionService) {
        this.expenseRepository = expenseRepository;
        this.subscriptionService = subscriptionService;
    }

    @Transactional(readOnly = true)
    public DashboardResponse getDashboard(Long userId) {
        LocalDate today = LocalDate.now();
        LocalDate monthStart = today.withDayOfMonth(1);
        LocalDate monthEnd = today.withDayOfMonth(today.lengthOfMonth());
        BigDecimal monthlyExpenses = expenseRepository.sumForUserBetween(userId, monthStart, monthEnd);
        List<CategorySummary> categories = expenseRepository
                .sumByCategoryForUserBetween(userId, monthStart, monthEnd).stream()
                .map(row -> new CategorySummary((ExpenseCategory) row[0], (BigDecimal) row[1])).toList();
        List<Subscription> active = subscriptionService.activeSubscriptions(userId);
        return new DashboardResponse(monthlyExpenses, active.size(), subscriptionService.monthlyCost(active),
                subscriptionService.yearlyCost(active), subscriptionService.upcoming(userId), categories);
    }
}