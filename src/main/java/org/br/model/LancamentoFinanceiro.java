package org.br.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "lancamentos_financeiros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LancamentoFinanceiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoLancamento tipo;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @CreationTimestamp
    private LocalDateTime dataLancamento;

    private LocalDate dataVencimento;
    
    private LocalDateTime dataPagamento; // Data em que foi efetivamente pago/recebido

    @ManyToOne
    @JoinColumn(name = "id_os")
    private OrdensServico ordemServico; // Vínculo opcional com uma O.S.

    public enum TipoLancamento {
        RECEITA, DESPESA
    }
}
