package com.ledgerly.controller;

import com.ledgerly.dto.SubscriptionRequest;
import com.ledgerly.dto.SubscriptionResponse;
import com.ledgerly.security.UserPrincipal;
import com.ledgerly.service.SubscriptionService;
import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping
    public ResponseEntity<SubscriptionResponse> create(@AuthenticationPrincipal UserPrincipal principal,
                                                        @Valid @RequestBody SubscriptionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subscriptionService.create(principal.getId(), request));
    }

    @GetMapping
    public List<SubscriptionResponse> list(@AuthenticationPrincipal UserPrincipal principal) {
        return subscriptionService.list(principal.getId());
    }

    @GetMapping("/upcoming")
    public List<SubscriptionResponse> upcoming(@AuthenticationPrincipal UserPrincipal principal) {
        return subscriptionService.upcoming(principal.getId());
    }

    @GetMapping("/{id}")
    public SubscriptionResponse get(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long id) {
        return subscriptionService.get(principal.getId(), id);
    }

    @PutMapping("/{id}")
    public SubscriptionResponse update(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long id,
                                       @Valid @RequestBody SubscriptionRequest request) {
        return subscriptionService.update(principal.getId(), id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long id) {
        subscriptionService.cancel(principal.getId(), id);
        return ResponseEntity.noContent().build();
    }
}