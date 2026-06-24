package org.br.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ordens_servico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrdensServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_os")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_veiculo", nullable = false)
    private Veiculo veiculo;

    @ManyToOne
    @JoinColumn(name = "id_mecanico")
    private Mecanico mecanico;

    @CreationTimestamp
    @Column(name = "data_abertura", updatable = false)
    private LocalDateTime dataAbertura;

    @Column(name = "data_previsao_entrega")
    private LocalDateTime dataPrevisaoEntrega;

    @Column(name = "data_encerramento")
    private LocalDateTime dataEncerramento;

    @Column(name = "inicio_atendimento")
    private LocalDateTime inicioAtendimento;

    @Column(name = "fim_atendimento")
    private LocalDateTime fimAtendimento;

    @Column(name = "quilometragem_entrada")
    private int quilometragemEntrada;

    @Column(name = "nivel_combustivel")
    private String nivelCombustivel;

    @Column(name = "checklist_avarias", columnDefinition = "TEXT")
    private String checklistAvarias;

    @Column(name = "pertences_internos", columnDefinition = "TEXT")
    private String pertencesInternos;

    @Column(name = "relato_cliente", columnDefinition = "TEXT")
    private String relatoCliente;

    @Column(name = "parecer_tecnico", columnDefinition = "TEXT")
    private String parecerTecnico;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusOS status;

    @Column(name = "forma_pagamento")
    private String formaPagamento;

    @Column(name = "valor_total_servicos", precision = 10, scale = 2)
    private BigDecimal valorTotalServicos;

    @Column(name = "valor_total_pecas", precision = 10, scale = 2)
    private BigDecimal valorTotalPecas;

    @Column(precision = 10, scale = 2)
    private BigDecimal desconto;

    @Column(name = "termo_garantia", columnDefinition = "TEXT")
    private String termoGarantia;

    @OneToMany(mappedBy = "ordemServico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OsServicos> servicos;

    @OneToMany(mappedBy = "ordemServico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OsPecas> pecas;

    @Transient
    public BigDecimal getValorGeralTotal() {
        BigDecimal totalServicos = valorTotalServicos != null ? valorTotalServicos : BigDecimal.ZERO;
        BigDecimal totalPecas = valorTotalPecas != null ? valorTotalPecas : BigDecimal.ZERO;
        BigDecimal desc = desconto != null ? desconto : BigDecimal.ZERO;
        return totalServicos.add(totalPecas).subtract(desc);
    }
}
