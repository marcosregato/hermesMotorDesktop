package org.br.mapper;

import org.br.dto.ClienteDTO;
import org.br.model.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public ClienteDTO toDTO(Cliente entity) {
        if (entity == null) {
            return null;
        }
        return ClienteDTO.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .cpfCnpj(entity.getCpfCnpj())
                .telefone(entity.getTelefone())
                .email(entity.getEmail())
                .endereco(entity.getEndereco())
                .build();
    }

    public Cliente toEntity(ClienteDTO dto) {
        if (dto == null) {
            return null;
        }
        return Cliente.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .cpfCnpj(dto.getCpfCnpj())
                .telefone(dto.getTelefone())
                .email(dto.getEmail())
                .endereco(dto.getEndereco())
                .ativo(true) // Regra de negócio padrão ao criar/atualizar
                .build();
    }
}
