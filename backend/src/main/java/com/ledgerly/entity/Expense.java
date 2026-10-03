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
@Table(name = "expenses")
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 24)
    private ExpenseCategory category;

    @Column(length = 255)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 24)
    private PaymentMethod paymentMethod;

    @Column(nullable = false)
    private LocalDate expenseDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    protected Expense() {
    }

    public Expense(BigDecimal amount, ExpenseCategory category, String description,
                   PaymentMethod paymentMethod, LocalDate expenseDate, User user) {
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.paymentMethod = paymentMethod;
        this.expenseDate = expenseDate;
        this.user = user;
    }

    public void update(BigDecimal amount, ExpenseCategory category, String description,
                       PaymentMethod paymentMethod, LocalDate expenseDate) {
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.paymentMethod = paymentMethod;
        this.expenseDate = expenseDate;
    }

    public Long getId() { return id; }
    public BigDecimal getAmount() { return amount; }
    public ExpenseCategory getCategory() { return category; }
    public String getDescription() { return description; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public LocalDate getExpenseDate() { return expenseDate; }
    public User getUser() { return user; }
}