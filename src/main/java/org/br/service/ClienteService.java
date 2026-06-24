package org.br.service;

import org.br.interfaceDao.ClienteInterface;
import org.br.dto.ClienteDTO;
import org.br.model.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteInterface clienteDAO;

    public List<ClienteDTO> listarAtivos() {
        return clienteDAO.findByAtivoTrue().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public ClienteDTO salvar(ClienteDTO dto) {
        Cliente cliente = toEntity(dto);
        cliente.setAtivo(true);
        return toDTO(clienteDAO.save(cliente));
    }

    public void excluirLogico(Long id) {
        Cliente cliente = clienteDAO.findById(id).orElseThrow();
        cliente.setAtivo(false);
        clienteDAO.save(cliente);
    }

    private ClienteDTO toDTO(Cliente entity) {
        return ClienteDTO.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .cpfCnpj(entity.getCpfCnpj())
                .telefone(entity.getTelefone())
                .email(entity.getEmail())
                .endereco(entity.getEndereco())
                .dataCadastro(entity.getDataCadastro())
                .build();
    }

    private Cliente toEntity(ClienteDTO dto) {
        return Cliente.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .cpfCnpj(dto.getCpfCnpj())
                .telefone(dto.getTelefone())
                .email(dto.getEmail())
                .endereco(dto.getEndereco())
                .build();
    }
}
