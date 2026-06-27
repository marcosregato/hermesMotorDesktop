package org.br.mapper;

import org.br.dto.MecanicoDTO;
import org.br.model.Mecanico;
import org.springframework.stereotype.Component;

@Component
public class MecanicoMapper {

    public MecanicoDTO toDTO(Mecanico entity) {
        if (entity == null) return null;
        return MecanicoDTO.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .cpf(entity.getCpf())
                .telefone(entity.getTelefone())
                .especialidade(entity.getEspecialidade())
                .ativo(entity.getAtivo())
                .build();
    }

    public Mecanico toEntity(MecanicoDTO dto) {
        if (dto == null) return null;
        return Mecanico.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .cpf(dto.getCpf())
                .telefone(dto.getTelefone())
                .especialidade(dto.getEspecialidade())
                .ativo(dto.getAtivo() != null ? dto.getAtivo() : true)
                .build();
    }
}
