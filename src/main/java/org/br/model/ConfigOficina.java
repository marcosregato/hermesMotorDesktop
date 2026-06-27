package org.br.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "config_oficina")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfigOficina {
    @Id
    private Long id; // Fixo como 1

    private String nomeOficina;
    private String cnpj;
    private String telefone;
    private String endereco;
    private String logoPath;

    @Column(columnDefinition = "TEXT")
    private String mensagemPadraoWhatsapp;

    // --- Novos Campos Fiscais ---
    private String tokenApiFiscal;

    @Enumerated(EnumType.STRING)
    private AmbienteFiscal ambienteFiscal;

    private String cfopPadrao;

    public enum AmbienteFiscal {
        HOMOLOGACAO, PRODUCAO
    }
}
