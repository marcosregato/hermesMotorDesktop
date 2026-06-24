package org.br.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "estoque_movimentacoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstoqueMovimentacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_peca")
    private PecasCatalogo peca;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimentacao tipo;

    @Column(nullable = false)
    private Integer quantidade;

    @CreationTimestamp
    private LocalDateTime dataMovimentacao;

    private String observacao; // Ex: "NF-e 12345", "Saída para OS #58", "Ajuste de inventário"

    public enum TipoMovimentacao {
        ENTRADA, SAIDA_OS, AJUSTE_MANUAL
    }
}
