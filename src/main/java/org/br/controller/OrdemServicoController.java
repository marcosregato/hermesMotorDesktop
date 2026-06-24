package org.br.controller;

import org.br.dto.OrdensServicoDTO;
import org.br.dto.OsItemDTO;
import org.br.model.StatusOS;
import org.br.service.OrdemServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrdemServicoController {

    @Autowired
    private OrdemServicoService osService;

    public List<OrdensServicoDTO> listarTodas() {
        return osService.listarTodas();
    }

    public OrdensServicoDTO buscarPorId(Long id) {
        return osService.buscarPorId(id);
    }

    public OrdensServicoDTO salvar(OrdensServicoDTO osDTO) {
        return osService.salvar(osDTO);
    }

    public void adicionarServico(Long idOs, Long idServico, int qtd) {
        osService.adicionarServico(idOs, idServico, qtd);
    }

    public void adicionarPeca(Long idOs, Long idPeca, int qtd) {
        osService.adicionarPeca(idOs, idPeca, qtd);
    }

    public List<OsItemDTO> listarItensDaOS(Long idOs) {
        return osService.listarItensDaOS(idOs);
    }

    public void removerItem(Long id, String tipo) {
        osService.removerItem(id, tipo);
    }

    public void alterarStatus(Long idOs, StatusOS novoStatus) {
        osService.alterarStatus(idOs, novoStatus);
    }

    public void iniciarAtendimento(Long idOs) {
        osService.iniciarAtendimento(idOs);
    }

    public void finalizarAtendimento(Long idOs) {
        osService.finalizarAtendimento(idOs);
    }
}
