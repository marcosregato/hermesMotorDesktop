package org.br.service;

import lombok.extern.slf4j.Slf4j;
import org.br.dto.FinanceiroSaldosDTO;
import org.br.dto.LancamentoFinanceiroDTO;
import org.br.interfaceDao.LancamentoFinanceiroInterface;
import org.br.model.LancamentoFinanceiro;
import org.br.model.OrdensServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FinanceiroService {

    private final LancamentoFinanceiroInterface lancamentoDAO;

    @Autowired
    public FinanceiroService(LancamentoFinanceiroInterface lancamentoDAO) {
        this.lancamentoDAO = lancamentoDAO;
    }

    public List<LancamentoFinanceiroDTO> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return lancamentoDAO.findByDataLancamentoBetween(inicio, fim).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public FinanceiroSaldosDTO getSaldos() {
        LocalDateTime inicioDia = LocalDate.now().atStartOfDay();
        LocalDateTime fimDia = LocalDate.now().atTime(LocalTime.MAX);
        
        LocalDateTime inicioMes = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime fimMes = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).atTime(LocalTime.MAX);

        return FinanceiroSaldosDTO.builder()
                .saldoDoDia(lancamentoDAO.sumByDataPagamentoBetween(inicioDia, fimDia))
                .saldoDoMes(lancamentoDAO.sumByDataPagamentoBetween(inicioMes, fimMes))
                .contasAReceber(lancamentoDAO.sumContasAReceber())
                .build();
    }

    @Transactional
    public void criarReceitaDeOS(OrdensServico os) {
        LancamentoFinanceiro lancamento = LancamentoFinanceiro.builder()
                .descricao("Receita da O.S. #" + os.getId())
                .tipo(LancamentoFinanceiro.TipoLancamento.RECEITA)
                .valor(os.getValorGeralTotal())
                .ordemServico(os)
                .dataVencimento(LocalDate.now())
                .build();
        lancamentoDAO.save(lancamento);
    }

    @Transactional
    public void criarDespesa(String descricao, BigDecimal valor, LocalDate vencimento) {
        LancamentoFinanceiro lancamento = LancamentoFinanceiro.builder()
                .descricao(descricao)
                .tipo(LancamentoFinanceiro.TipoLancamento.DESPESA)
                .valor(valor)
                .dataVencimento(vencimento)
                .build();
        lancamentoDAO.save(lancamento);
    }

    @Transactional
    public void darBaixa(Long idLancamento) {
        LancamentoFinanceiro lancamento = lancamentoDAO.findById(idLancamento).orElseThrow();
        if (lancamento.getDataPagamento() != null) return;
        
        lancamento.setDataPagamento(LocalDateTime.now());
        lancamentoDAO.save(lancamento);
    }

    private LancamentoFinanceiroDTO toDTO(LancamentoFinanceiro entity) {
        String status = entity.getDataPagamento() != null ? "PAGO" : "PENDENTE";
        if (entity.getDataPagamento() == null && entity.getDataVencimento().isBefore(LocalDate.now())) {
            status = "VENCIDO";
        }
        return LancamentoFinanceiroDTO.builder()
                .id(entity.getId())
                .descricao(entity.getDescricao())
                .tipo(entity.getTipo())
                .valor(entity.getValor())
                .dataLancamento(entity.getDataLancamento())
                .dataVencimento(entity.getDataVencimento())
                .dataPagamento(entity.getDataPagamento())
                .status(status)
                .build();
    }
}
