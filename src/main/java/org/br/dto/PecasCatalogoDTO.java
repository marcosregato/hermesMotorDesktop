package org.br.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PecasCatalogoDTO {
    private Long id;
    private String codigoPeca;
    private String nome;
    private String marca;
    private BigDecimal precoVenda;
    private int quantidadeEstoque;

    @Override
    public String toString() {
        return nome + " (" + codigoPeca + ")";
    }
}
