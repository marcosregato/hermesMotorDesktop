package org.br.mapper;

import org.br.dto.VeiculoDTO;
import org.br.interfaceDao.ClienteInterface;
import org.br.model.Cliente;
import org.br.model.Veiculo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VeiculoMapper {

    @Autowired
    private ClienteInterface clienteDAO;

    public VeiculoDTO toDTO(Veiculo entity) {
        if (entity == null) return null;
        return VeiculoDTO.builder()
                .id(entity.getId())
                .idCliente(entity.getCliente().getId())
                .placa(entity.getPlaca())
                .marca(entity.getMarca())
                .modelo(entity.getModelo())
                .anoFabricacao(entity.getAnoFabricacao())
                .anoModelo(entity.getAnoModelo())
                .cor(entity.getCor())
                .build();
    }

    public Veiculo toEntity(VeiculoDTO dto) {
        if (dto == null) return null;
        Cliente cliente = clienteDAO.findById(dto.getIdCliente()).orElseThrow(() -> new RuntimeException("Cliente não encontrado para o veículo."));
        return Veiculo.builder()
                .id(dto.getId())
                .cliente(cliente)
                .placa(dto.getPlaca())
                .marca(dto.getMarca())
                .modelo(dto.getModelo())
                .anoFabricacao(dto.getAnoFabricacao())
                .anoModelo(dto.getAnoModelo())
                .cor(dto.getCor())
                .ativo(true)
                .build();
    }
}
