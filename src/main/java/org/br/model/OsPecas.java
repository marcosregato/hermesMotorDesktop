package org.br.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "os_pecas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OsPecas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_os_peca")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_os", nullable = false)
    private OrdensServico ordemServico;

    @ManyToOne
    @JoinColumn(name = "id_peca", nullable = false)
    private PecasCatalogo peca;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(name = "valor_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorUnitario;
}
