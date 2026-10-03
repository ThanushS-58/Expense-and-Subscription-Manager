package com.ledgerly.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "subscriptions")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private BillingCycle billingCycle;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(nullable = false)
    private LocalDate nextBillingDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private SubscriptionStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    protected Subscription() {
    }

    public Subscription(String name, BigDecimal amount, BillingCycle billingCycle, String category,
                        LocalDate nextBillingDate, User user) {
        this.name = name;
        this.amount = amount;
        this.billingCycle = billingCycle;
        this.category = category;
        this.nextBillingDate = nextBillingDate;
        this.status = SubscriptionStatus.ACTIVE;
        this.user = user;
    }

    public void update(String name, BigDecimal amount, BillingCycle billingCycle, String category,
                       LocalDate nextBillingDate) {
        this.name = name;
        this.amount = amount;
        this.billingCycle = billingCycle;
        this.category = category;
        this.nextBillingDate = nextBillingDate;
    }

    public void cancel() { this.status = SubscriptionStatus.CANCELLED; }
    public Long getId() { return id; }
    public String getName() { return name; }
    public BigDecimal getAmount() { return amount; }
    public BillingCycle getBillingCycle() { return billingCycle; }
    public String getCategory() { return category; }
    public LocalDate getNextBillingDate() { return nextBillingDate; }
    public SubscriptionStatus getStatus() { return status; }
    public User getUser() { return user; }
}