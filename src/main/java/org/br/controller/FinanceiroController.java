package org.br.controller;

import org.br.dto.FinanceiroSaldosDTO;
import org.br.dto.LancamentoFinanceiroDTO;
import org.br.service.FinanceiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class FinanceiroController {

    private final FinanceiroService financeiroService;

    @Autowired
    public FinanceiroController(FinanceiroService financeiroService) {
        this.financeiroService = financeiroService;
    }

    public List<LancamentoFinanceiroDTO> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return financeiroService.listarPorPeriodo(inicio, fim);
    }

    public FinanceiroSaldosDTO getSaldos() {
        return financeiroService.getSaldos();
    }

    public void criarDespesa(String descricao, BigDecimal valor, LocalDate vencimento) {
        financeiroService.criarDespesa(descricao, valor, vencimento);
    }

    public void darBaixa(Long idLancamento) {
        financeiroService.darBaixa(idLancamento);
    }
}
