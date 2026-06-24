package org.br.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "os_servicos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OsServicos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_os_servico")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_os", nullable = false)
    private OrdensServico ordemServico;

    @ManyToOne
    @JoinColumn(name = "id_servico", nullable = false)
    private ServicosCatalogo servico;

    @Column(nullable = false)
    private Integer quantidade = 1;

    @Column(name = "valor_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorUnitario;
}
