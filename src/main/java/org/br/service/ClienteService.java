package org.br.service;

import lombok.extern.slf4j.Slf4j;
import org.br.dto.ClienteDTO;
import org.br.interfaceDao.ClienteInterface;
import org.br.mapper.ClienteMapper;
import org.br.model.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ClienteService {

    private final ClienteInterface clienteDAO;
    private final ClienteMapper clienteMapper;

    @Autowired
    public ClienteService(ClienteInterface clienteDAO, ClienteMapper clienteMapper) {
        this.clienteDAO = clienteDAO;
        this.clienteMapper = clienteMapper;
    }

    public List<ClienteDTO> listarAtivos() {
        log.info("Serviço: Buscando todos os clientes com status ativo.");
        return clienteDAO.findByAtivoTrue().stream()
                .map(clienteMapper::toDTO) // Usa o mapper
                .collect(Collectors.toList());
    }

    @Transactional
    public ClienteDTO salvar(ClienteDTO dto) {
        log.info("Serviço: Salvando cliente ID: {}", dto.getId());
        Cliente cliente = clienteMapper.toEntity(dto); // Usa o mapper
        Cliente salvo = clienteDAO.save(cliente);
        return clienteMapper.toDTO(salvo); // Usa o mapper
    }

    @Transactional
    public void excluirLogico(Long id) {
        log.warn("Serviço: Realizando exclusão lógica do cliente ID: {}", id);
        clienteDAO.findById(id).ifPresent(cliente -> {
            cliente.setAtivo(false);
            clienteDAO.save(cliente);
            log.info("Cliente ID: {} desativado com sucesso.", id);
        });
    }
}
