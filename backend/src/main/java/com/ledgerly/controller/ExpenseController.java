package com.ledgerly.controller;

import com.ledgerly.dto.ExpenseRequest;
import com.ledgerly.dto.ExpenseResponse;
import com.ledgerly.entity.ExpenseCategory;
import com.ledgerly.security.UserPrincipal;
import com.ledgerly.service.ExpenseService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) { this.expenseService = expenseService; }

    @PostMapping
    public ResponseEntity<ExpenseResponse> create(@AuthenticationPrincipal UserPrincipal principal,
                                                   @Valid @RequestBody ExpenseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(expenseService.create(principal.getId(), request));
    }

    @GetMapping
    public List<ExpenseResponse> list(@AuthenticationPrincipal UserPrincipal principal,
                                     @RequestParam(required = false) ExpenseCategory category,
                                     @RequestParam(required = false) LocalDate fromDate,
                                     @RequestParam(required = false) LocalDate toDate) {
        return expenseService.list(principal.getId(), category, fromDate, toDate);
    }

    @GetMapping("/{id}")
    public ExpenseResponse get(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long id) {
        return expenseService.get(principal.getId(), id);
    }

    @PutMapping("/{id}")
    public ExpenseResponse update(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long id,
                                  @Valid @RequestBody ExpenseRequest request) {
        return expenseService.update(principal.getId(), id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long id) {
        expenseService.delete(principal.getId(), id);
        return ResponseEntity.noContent().build();
    }
}