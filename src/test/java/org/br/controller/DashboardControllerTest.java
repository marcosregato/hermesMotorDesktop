package org.br.controller;

import org.br.dto.DashboardDTO;
import org.br.service.DashboardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DashboardControllerTest {

    @Mock
    private DashboardService dashboardService;

    @InjectMocks
    private DashboardController dashboardController;

    @Test
    void getDadosDashboard_DeveRetornarDTO() {
        DashboardDTO dto = DashboardDTO.builder().faturamentoMes(BigDecimal.TEN).build();
        when(dashboardService.getDadosDashboard()).thenReturn(dto);

        DashboardDTO resultado = dashboardController.getDadosDashboard();

        assertEquals(BigDecimal.TEN, resultado.getFaturamentoMes());
        verify(dashboardService, times(1)).getDadosDashboard();
    }
}
