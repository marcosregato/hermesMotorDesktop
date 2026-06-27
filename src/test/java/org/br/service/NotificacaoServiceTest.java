package org.br.service;

import org.br.interfaceDao.OrdensServicoInterface;
import org.br.model.Veiculo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NotificacaoServiceTest {

    @Mock
    private OrdensServicoInterface osDAO;

    @InjectMocks
    private NotificacaoService notificacaoService;

    @Test
    void getVeiculosParaLembrete_DeveChamarRepositorioComDataCorreta() {
        when(osDAO.findVeiculosParaLembreteRevisao(any(LocalDateTime.class))).thenReturn(Collections.emptyList());

        List<Veiculo> resultado = notificacaoService.getVeiculosParaLembrete();

        assertNotNull(resultado);
        verify(osDAO).findVeiculosParaLembreteRevisao(any(LocalDateTime.class));
    }
}
