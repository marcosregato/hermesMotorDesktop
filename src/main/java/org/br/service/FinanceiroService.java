package org.br.service;

import lombok.extern.slf4j.Slf4j;
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
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FinanceiroService {

    @Autowired
    private LancamentoFinanceiroInterface lancamentoDAO;

    public List<LancamentoFinanceiroDTO> listarTodos() {
        return lancamentoDAO.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void criarReceitaDeOS(OrdensServico os) {
        log.info("Criando lançamento de RECEITA para a O.S. {}", os.getId());
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
        log.info("Criando lançamento de DESPESA: {}", descricao);
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
        log.info("Dando baixa no lançamento ID: {}", idLancamento);
        LancamentoFinanceiro lancamento = lancamentoDAO.findById(idLancamento)
                .orElseThrow(() -> new RuntimeException("Lançamento não encontrado"));
        
        if (lancamento.getDataPagamento() != null) {
            log.warn("Lançamento {} já possui baixa.", idLancamento);
            return;
        }
        
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
