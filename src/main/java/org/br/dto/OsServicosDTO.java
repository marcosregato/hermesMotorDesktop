package org.br.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OsServicosDTO {
    private Long id;
    private Long idOrdemServico;
    private Long idServico;
    private Integer quantidade;
    private BigDecimal valorUnitario;
}
