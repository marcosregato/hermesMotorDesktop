package org.br.controller;

import org.br.dto.LancamentoFinanceiroDTO;
import org.br.service.FinanceiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class FinanceiroController {

    @Autowired
    private FinanceiroService financeiroService;

    public List<LancamentoFinanceiroDTO> listarTodos() {
        return financeiroService.listarTodos();
    }

    public void criarDespesa(String descricao, BigDecimal valor, LocalDate vencimento) {
        financeiroService.criarDespesa(descricao, valor, vencimento);
    }

    public void darBaixa(Long idLancamento) {
        financeiroService.darBaixa(idLancamento);
    }
}
