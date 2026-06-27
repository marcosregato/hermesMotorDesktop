package org.br.controller;

import lombok.extern.slf4j.Slf4j;
import org.br.dto.OrdensServicoDTO;
import org.br.dto.OsItemDTO;
import org.br.model.StatusOS;
import org.br.service.OrdemServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class OrdemServicoController {

    private final OrdemServicoService osService;

    @Autowired
    public OrdemServicoController(OrdemServicoService osService) {
        this.osService = osService;
    }

    public List<OrdensServicoDTO> listarTodas() {
        log.info("Controlador: Listando todas as Ordens de Serviço.");
        return osService.listarTodas();
    }

    public OrdensServicoDTO buscarPorId(Long id) {
        log.info("Controlador: Buscando O.S. de ID: {}", id);
        return osService.buscarPorId(id);
    }

    public OrdensServicoDTO salvar(OrdensServicoDTO osDTO) {
        log.info("Controlador: Salvando O.S. para veículo ID: {}", osDTO.getIdVeiculo());
        return osService.salvar(osDTO);
    }

    public void adicionarServico(Long idOs, Long idServico, int qtd) {
        log.info("Controlador: Adicionando serviço {} à O.S. {}", idServico, idOs);
        osService.adicionarServico(idOs, idServico, qtd);
    }

    public void adicionarPeca(Long idOs, Long idPeca, int qtd) {
        log.info("Controlador: Adicionando peça {} à O.S. {}", idPeca, idOs);
        osService.adicionarPeca(idOs, idPeca, qtd);
    }

    public List<OsItemDTO> listarItensDaOS(Long idOs) {
        log.info("Controlador: Listando itens da O.S. {}", idOs);
        return osService.listarItensDaOS(idOs);
    }

    public void removerItem(Long id, String tipo) {
        log.warn("Controlador: Removendo item {} de ID {} da O.S.", tipo, id);
        osService.removerItem(id, tipo);
    }

    public void alterarStatus(Long idOs, StatusOS novoStatus) {
        log.info("Controlador: Alterando status da O.S. {} para {}", idOs, novoStatus);
        osService.alterarStatus(idOs, novoStatus);
    }

    public void iniciarAtendimento(Long idOs) {
        log.info("Controlador: Iniciando atendimento para O.S. {}", idOs);
        osService.iniciarAtendimento(idOs);
    }

    public void finalizarAtendimento(Long idOs) {
        log.info("Controlador: Finalizando atendimento para O.S. {}", idOs);
        osService.finalizarAtendimento(idOs);
    }
}
