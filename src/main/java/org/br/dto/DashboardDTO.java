package org.br.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashboardDTO {
    private long motosNoPatio;
    private long orçamentosPendentes;
    private BigDecimal faturamentoMes;
    private List<PecasCatalogoDTO> pecasEstoqueBaixo;
}
