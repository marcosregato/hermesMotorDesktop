package org.br.controller;

import org.br.model.StatusOS;
import org.br.service.OrdemServicoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class OrdemServicoControllerTest {

    @Mock
    private OrdemServicoService osService;

    @InjectMocks
    private OrdemServicoController osController;

    @Test
    void alterarStatus_DeveChamarService() {
        osController.alterarStatus(1L, StatusOS.PRONTO);
        verify(osService, times(1)).alterarStatus(1L, StatusOS.PRONTO);
    }

    @Test
    void adicionarServico_DeveChamarService() {
        osController.adicionarServico(1L, 2L, 1);
        verify(osService, times(1)).adicionarServico(1L, 2L, 1);
    }
}
