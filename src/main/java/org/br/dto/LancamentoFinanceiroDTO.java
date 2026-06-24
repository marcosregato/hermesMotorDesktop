package org.br.dto;

import lombok.Builder;
import lombok.Data;
import org.br.model.LancamentoFinanceiro;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class LancamentoFinanceiroDTO {
    private Long id;
    private String descricao;
    private LancamentoFinanceiro.TipoLancamento tipo;
    private BigDecimal valor;
    private LocalDateTime dataLancamento;
    private LocalDate dataVencimento;
    private LocalDateTime dataPagamento;
    private String status;
}
