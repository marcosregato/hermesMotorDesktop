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
    private Long id; // Geralmente fixo como 1
    private String nomeOficina;
    private String cnpj;
    private String telefone;
    private String endereco;
    @Column(columnDefinition = "TEXT")
    private String mensagemPadraoWhatsapp;
    private String logoPath; // Novo campo para o caminho do logo
}
