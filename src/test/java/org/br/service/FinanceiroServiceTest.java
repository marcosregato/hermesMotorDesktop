package org.br.service;

import org.br.dto.FinanceiroSaldosDTO;
import org.br.interfaceDao.LancamentoFinanceiroInterface;
import org.br.model.LancamentoFinanceiro;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FinanceiroServiceTest {

    @Mock
    private LancamentoFinanceiroInterface lancamentoDAO;

    @InjectMocks
    private FinanceiroService financeiroService;

    @Test
    void getSaldos_DeveChamarRepositorioCorretamente() {
        when(lancamentoDAO.sumByDataPagamentoBetween(any(), any())).thenReturn(new BigDecimal("100.00"));
        when(lancamentoDAO.sumContasAReceber()).thenReturn(new BigDecimal("50.00"));

        FinanceiroSaldosDTO saldos = financeiroService.getSaldos();

        assertEquals(new BigDecimal("100.00"), saldos.getSaldoDoDia());
        assertEquals(new BigDecimal("50.00"), saldos.getContasAReceber());
    }

    @Test
    void darBaixa_DeveDefinirDataPagamento() {
        LancamentoFinanceiro lancamento = LancamentoFinanceiro.builder().id(1L).build();
        when(lancamentoDAO.findById(1L)).thenReturn(Optional.of(lancamento));

        financeiroService.darBaixa(1L);

        assertNotNull(lancamento.getDataPagamento());
        verify(lancamentoDAO).save(lancamento);
    }
}
