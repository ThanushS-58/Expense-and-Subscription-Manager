package com.ledgerly.repository;

import com.ledgerly.entity.Subscription;
import com.ledgerly.entity.SubscriptionStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByUserIdOrderByNextBillingDateAsc(Long userId);
    Optional<Subscription> findByIdAndUserId(Long id, Long userId);
    List<Subscription> findByUserIdAndStatus(Long userId, SubscriptionStatus status);
    List<Subscription> findByUserIdAndStatusAndNextBillingDateBetweenOrderByNextBillingDateAsc(
            Long userId, SubscriptionStatus status, LocalDate startDate, LocalDate endDate);
}