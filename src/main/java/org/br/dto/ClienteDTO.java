package org.br.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteDTO {
    private Long id;
    private String nome;
    private String cpfCnpj;
    private String telefone;
    private String email;
    private String endereco;
    private LocalDateTime dataCadastro;

    @Override
    public String toString() {
        return nome + " (" + cpfCnpj + ")";
    }
}
