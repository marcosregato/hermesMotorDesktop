package org.br.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MecanicoDTO {
    private Long id;
    private String nome;
    private String cpf;
    private String telefone;
    private String especialidade;
    private Boolean ativo;

    @Override
    public String toString() {
        return nome + (especialidade != null ? " (" + especialidade + ")" : "");
    }
}
