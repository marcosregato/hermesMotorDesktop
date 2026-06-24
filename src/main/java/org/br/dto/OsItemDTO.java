package org.br.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OsItemDTO {
    private Long id;
    private String tipo; // "PECA" ou "SERVICO"
    private String descricao;
    private Integer quantidade;
    private BigDecimal valorUnitario;
    private BigDecimal subtotal;
}
