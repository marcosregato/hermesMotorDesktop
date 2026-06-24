package org.br.service;

import org.br.dto.DashboardDTO;
import org.br.interfaceDao.OrdensServicoInterface;
import org.br.interfaceDao.PecasCatalogoInterface;
import org.br.model.StatusOS;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DashboardServiceTest {

    @Mock
    private OrdensServicoInterface osDAO;

    @Mock
    private PecasCatalogoInterface pecasDAO;

    @InjectMocks
    private DashboardService dashboardService;

    @Test
    void getDadosDashboard_DeveCalcularMotosNoPatio() {
        when(osDAO.countByStatusIn(anyList())).thenReturn(5L);
        when(osDAO.countByStatus(StatusOS.ORCAMENTO)).thenReturn(2L);
        when(pecasDAO.findEstoqueBaixo()).thenReturn(Collections.emptyList());

        DashboardDTO resultado = dashboardService.getDadosDashboard();

        assertEquals(5, resultado.getMotosNoPatio());
        assertEquals(2, resultado.getOrçamentosPendentes());
    }
}
