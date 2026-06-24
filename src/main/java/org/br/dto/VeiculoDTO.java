package org.br.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VeiculoDTO {
    private Long id;
    private Long idCliente;
    private String placa;
    private String marca;
    private String modelo;
    private int anoFabricacao;
    private int anoModelo;
    private String cor;

    @Override
    public String toString() {
        return placa + " - " + marca + " " + modelo;
    }
}
