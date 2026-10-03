package com.ledgerly.service;

import com.ledgerly.dto.SubscriptionRequest;
import com.ledgerly.dto.SubscriptionResponse;
import com.ledgerly.entity.BillingCycle;
import com.ledgerly.entity.Subscription;
import com.ledgerly.entity.SubscriptionStatus;
import com.ledgerly.entity.User;
import com.ledgerly.exception.ResourceNotFoundException;
import com.ledgerly.repository.SubscriptionRepository;
import com.ledgerly.repository.UserRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, UserRepository userRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public SubscriptionResponse create(Long userId, SubscriptionRequest request) {
        User user = userRepository.getReferenceById(userId);
        LocalDate nextDate = normalizeNextDate(request.nextBillingDate(), request.billingCycle());
        Subscription subscription = new Subscription(request.name().trim(), request.amount(),
                request.billingCycle(), request.category().trim(), nextDate, user);
        return toResponse(subscriptionRepository.save(subscription));
    }

    @Transactional(readOnly = true)
    public List<SubscriptionResponse> list(Long userId) {
        return subscriptionRepository.findByUserIdOrderByNextBillingDateAsc(userId).stream()
                .map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public SubscriptionResponse get(Long userId, Long id) { return toResponse(findOwned(id, userId)); }

    @Transactional
    public SubscriptionResponse update(Long userId, Long id, SubscriptionRequest request) {
        Subscription subscription = findOwned(id, userId);
        LocalDate nextDate = normalizeNextDate(request.nextBillingDate(), request.billingCycle());
        subscription.update(request.name().trim(), request.amount(), request.billingCycle(),
                request.category().trim(), nextDate);
        return toResponse(subscription);
    }

    @Transactional
    public void cancel(Long userId, Long id) { findOwned(id, userId).cancel(); }

    @Transactional(readOnly = true)
    public List<SubscriptionResponse> upcoming(Long userId) {
        LocalDate today = LocalDate.now();
        return subscriptionRepository.findByUserIdAndStatusAndNextBillingDateBetweenOrderByNextBillingDateAsc(
                        userId, SubscriptionStatus.ACTIVE, today, today.plusDays(7)).stream()
                .map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<Subscription> activeSubscriptions(Long userId) {
        return subscriptionRepository.findByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE);
    }

    public BigDecimal monthlyCost(List<Subscription> subscriptions) {
        BigDecimal total = BigDecimal.ZERO;
        for (Subscription subscription : subscriptions) {
            if (subscription.getBillingCycle() == BillingCycle.MONTHLY) {
                total = total.add(subscription.getAmount());
            } else {
                BigDecimal monthlyAmount = subscription.getAmount()
                        .divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
                total = total.add(monthlyAmount);
            }
        }
        return total;
    }

    public BigDecimal yearlyCost(List<Subscription> subscriptions) {
        BigDecimal total = BigDecimal.ZERO;
        for (Subscription subscription : subscriptions) {
            if (subscription.getBillingCycle() == BillingCycle.YEARLY) {
                total = total.add(subscription.getAmount());
            } else {
                BigDecimal yearlyAmount = subscription.getAmount().multiply(BigDecimal.valueOf(12));
                total = total.add(yearlyAmount);
            }
        }
        return total;
    }

    private LocalDate normalizeNextDate(LocalDate billingDate, BillingCycle cycle) {
        LocalDate today = LocalDate.now();
        while (billingDate.isBefore(today)) {
            billingDate = cycle == BillingCycle.MONTHLY
                    ? billingDate.plusMonths(1)
                    : billingDate.plusYears(1);
        }
        return billingDate;
    }

    private Subscription findOwned(Long id, Long userId) {
        return subscriptionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found"));
    }

    public SubscriptionResponse toResponse(Subscription subscription) {
        return new SubscriptionResponse(subscription.getId(), subscription.getName(), subscription.getAmount(),
                subscription.getBillingCycle(), subscription.getCategory(), subscription.getNextBillingDate(),
                subscription.getStatus(), subscription.getUser().getId());
    }
}