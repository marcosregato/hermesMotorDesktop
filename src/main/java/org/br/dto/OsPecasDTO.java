package org.br.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OsPecasDTO {
    private Long id;
    private Long idOrdemServico;
    private Long idPeca;
    private Integer quantidade;
    private BigDecimal valorUnitario;
}
