package org.br.mapper;

import org.br.dto.OrdensServicoDTO;
import org.br.interfaceDao.MecanicoInterface;
import org.br.interfaceDao.VeiculoInterface;
import org.br.model.Mecanico;
import org.br.model.OrdensServico;
import org.br.model.Veiculo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OrdemServicoMapper {

    @Autowired
    private VeiculoInterface veiculoDAO;
    @Autowired
    private MecanicoInterface mecanicoDAO;

    public OrdensServicoDTO toDTO(OrdensServico entity) {
        if (entity == null) return null;
        return OrdensServicoDTO.builder()
                .id(entity.getId())
                .idVeiculo(entity.getVeiculo().getId())
                .idMecanico(entity.getMecanico() != null ? entity.getMecanico().getId() : null)
                .dataAbertura(entity.getDataAbertura())
                .dataEncerramento(entity.getDataEncerramento())
                .inicioAtendimento(entity.getInicioAtendimento())
                .fimAtendimento(entity.getFimAtendimento())
                .status(entity.getStatus())
                .valorTotalServicos(entity.getValorTotalServicos())
                .valorTotalPecas(entity.getValorTotalPecas())
                .desconto(entity.getDesconto())
                .valorGeralTotal(entity.getValorGeralTotal())
                .relatoCliente(entity.getRelatoCliente())
                .build();
    }

    public OrdensServico toEntity(OrdensServicoDTO dto) {
        if (dto == null) return null;
        
        Veiculo veiculo = veiculoDAO.findById(dto.getIdVeiculo())
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado para a O.S."));
        Mecanico mecanico = dto.getIdMecanico() != null ? mecanicoDAO.findById(dto.getIdMecanico()).orElse(null) : null;

        return OrdensServico.builder()
                .id(dto.getId())
                .veiculo(veiculo)
                .mecanico(mecanico)
                .relatoCliente(dto.getRelatoCliente())
                .quilometragemEntrada(dto.getQuilometragemEntrada())
                .nivelCombustivel(dto.getNivelCombustivel())
                .status(dto.getStatus())
                .valorTotalServicos(dto.getValorTotalServicos() != null ? dto.getValorTotalServicos() : BigDecimal.ZERO)
                .valorTotalPecas(dto.getValorTotalPecas() != null ? dto.getValorTotalPecas() : BigDecimal.ZERO)
                .desconto(dto.getDesconto() != null ? dto.getDesconto() : BigDecimal.ZERO)
                .dataEncerramento(dto.getDataEncerramento())
                .inicioAtendimento(dto.getInicioAtendimento())
                .fimAtendimento(dto.getFimAtendimento())
                .build();
    }
}
