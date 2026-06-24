package org.br.controller;

import org.br.dto.DashboardDTO;
import org.br.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DashboardController {

    private final DashboardService dashboardService;

    @Autowired
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    public DashboardDTO getDadosDashboard() {
        return dashboardService.getDadosDashboard();
    }
}
