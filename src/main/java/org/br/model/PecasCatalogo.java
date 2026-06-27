package org.br.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "pecas_catalogo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PecasCatalogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_peca")
    private Long id;

    @Column(name = "codigo_peca", length = 50, unique = true)
    private String codigoPeca;

    @Column(length = 100, nullable = false)
    private String nome;

    @Column(length = 50)
    private String marca;

    @Column(name = "preco_venda", precision = 10, scale = 2)
    private BigDecimal precoVenda;

    @Column(name = "quantidade_estoque")
    private int quantidadeEstoque;

    // --- Novos Campos Fiscais ---
    @Column(length = 8)
    private String ncm; // Nomenclatura Comum do Mercosul

    @Column(length = 1)
    private String origem; // Origem da mercadoria (0 para Nacional, 1 para Estrangeira, etc.)
}
