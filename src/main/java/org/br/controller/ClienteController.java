package org.br.controller;

import lombok.extern.slf4j.Slf4j;
import org.br.dto.ClienteDTO;
import org.br.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ClienteController {

    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public List<ClienteDTO> listarTodos() {
        log.info("Controlador: Listando todos os clientes ativos.");
        return clienteService.listarAtivos();
    }

    public ClienteDTO salvar(ClienteDTO clienteDTO) {
        log.info("Controlador: Solicitando salvamento de cliente: {}", clienteDTO.getNome());
        return clienteService.salvar(clienteDTO);
    }

    public void excluir(Long id) {
        log.warn("Controlador: Solicitando exclusão lógica do cliente ID: {}", id);
        clienteService.excluirLogico(id);
    }
}
