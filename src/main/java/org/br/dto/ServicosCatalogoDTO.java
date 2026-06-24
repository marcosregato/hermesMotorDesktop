package org.br.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServicosCatalogoDTO {
    private Long id;
    private String descricao;
    private BigDecimal precoBase;

    @Override
    public String toString() {
        return descricao;
    }
}
