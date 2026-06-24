package org.br.dto;

import lombok.Builder;
import lombok.Data;
import org.br.model.EstoqueMovimentacao;

import java.time.LocalDateTime;

@Data
@Builder
public class EstoqueMovimentacaoDTO {
    private Long id;
    private String nomePeca;
    private EstoqueMovimentacao.TipoMovimentacao tipo;
    private Integer quantidade;
    private LocalDateTime dataMovimentacao;
    private String observacao;
}
