package org.br.service;

import org.br.interfaceDao.AgendamentoInterface;
import org.br.model.Agendamento;
import org.br.model.Cliente;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AgendamentoServiceTest {

    @Mock
    private AgendamentoInterface agendamentoDAO;

    @InjectMocks
    private AgendamentoService agendamentoService;

    @Test
    void buscarPorPeriodo_DeveChamarRepositorio() {
        LocalDateTime inicio = LocalDateTime.now().withHour(0);
        LocalDateTime fim = LocalDateTime.now().withHour(23);
        
        when(agendamentoDAO.findByDataHoraBetween(inicio, fim)).thenReturn(Collections.emptyList());

        List<Agendamento> resultado = agendamentoService.buscarPorPeriodo(inicio, fim);

        assertNotNull(resultado);
        verify(agendamentoDAO).findByDataHoraBetween(inicio, fim);
    }

    @Test
    void salvar_DeveDefinirStatusPadraoESalvar() {
        Agendamento agendamento = Agendamento.builder()
                .cliente(Cliente.builder().id(1L).build())
                .dataHora(LocalDateTime.now())
                .build();

        agendamentoService.salvar(agendamento);

        assertEquals(Agendamento.StatusAgendamento.AGENDADO, agendamento.getStatus());
        verify(agendamentoDAO).save(agendamento);
    }
}
