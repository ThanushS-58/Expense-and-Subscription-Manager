package com.ledgerly.service;

import com.ledgerly.dto.ExpenseRequest;
import com.ledgerly.dto.ExpenseResponse;
import com.ledgerly.entity.Expense;
import com.ledgerly.entity.ExpenseCategory;
import com.ledgerly.entity.User;
import com.ledgerly.exception.ResourceNotFoundException;
import com.ledgerly.repository.ExpenseRepository;
import com.ledgerly.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ExpenseResponse create(Long userId, ExpenseRequest request) {
        User user = userRepository.getReferenceById(userId);
        return toResponse(expenseRepository.save(new Expense(request.amount(), request.category(),
                request.description(), request.paymentMethod(), request.expenseDate(), user)));
    }

    @Transactional(readOnly = true)
    public List<ExpenseResponse> list(Long userId, ExpenseCategory category, LocalDate fromDate, LocalDate toDate) {
        if (fromDate != null && toDate != null && fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("Start date must not be after end date");
        }
        return expenseRepository.findForUser(userId, category, fromDate, toDate).stream()
                .map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ExpenseResponse get(Long userId, Long id) { return toResponse(findOwned(id, userId)); }

    @Transactional
    public ExpenseResponse update(Long userId, Long id, ExpenseRequest request) {
        Expense expense = findOwned(id, userId);
        expense.update(request.amount(), request.category(), request.description(),
                request.paymentMethod(), request.expenseDate());
        return toResponse(expense);
    }

    @Transactional
    public void delete(Long userId, Long id) { expenseRepository.delete(findOwned(id, userId)); }

    private Expense findOwned(Long id, Long userId) {
        return expenseRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
    }

    private ExpenseResponse toResponse(Expense expense) {
        return new ExpenseResponse(expense.getId(), expense.getAmount(), expense.getCategory(),
                expense.getDescription(), expense.getPaymentMethod(), expense.getExpenseDate(),
                expense.getUser().getId());
    }
}