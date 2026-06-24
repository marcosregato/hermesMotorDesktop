package org.br.dto;

import lombok.*;
import org.br.model.StatusOS;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdensServicoDTO {
    private Long id;
    private Long idVeiculo;
    private Long idMecanico;
    private LocalDateTime dataAbertura;
    private LocalDateTime dataPrevisaoEntrega;
    private LocalDateTime dataEncerramento;
    private LocalDateTime inicioAtendimento; // Campo adicionado
    private LocalDateTime fimAtendimento;    // Campo adicionado
    private int quilometragemEntrada;
    private String nivelCombustivel;
    private String checklistAvarias;
    private String pertencesInternos;
    private String relatoCliente;
    private String parecerTecnico;
    private StatusOS status;
    private String formaPagamento;
    private BigDecimal valorTotalServicos;
    private BigDecimal valorTotalPecas;
    private BigDecimal desconto;
    private BigDecimal valorGeralTotal;
    private String termoGarantia;
}
