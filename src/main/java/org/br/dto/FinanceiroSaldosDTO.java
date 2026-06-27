package org.br.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class FinanceiroSaldosDTO {
    private BigDecimal saldoDoDia;
    private BigDecimal saldoDoMes;
    private BigDecimal contasAReceber;
}
