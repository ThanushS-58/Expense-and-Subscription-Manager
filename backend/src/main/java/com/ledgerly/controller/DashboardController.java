package com.ledgerly.controller;

import com.ledgerly.dto.DashboardResponse;
import com.ledgerly.security.UserPrincipal;
import com.ledgerly.service.DashboardService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) { this.dashboardService = dashboardService; }

    @GetMapping
    public DashboardResponse getDashboard(@AuthenticationPrincipal UserPrincipal principal) {
        return dashboardService.getDashboard(principal.getId());
    }
}