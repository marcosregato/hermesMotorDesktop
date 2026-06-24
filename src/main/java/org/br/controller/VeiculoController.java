package org.br.controller;

import lombok.extern.slf4j.Slf4j;
import org.br.dto.OrdensServicoDTO;
import org.br.dto.VeiculoDTO;
import org.br.service.VeiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class VeiculoController {

    private final VeiculoService veiculoService;

    @Autowired
    public VeiculoController(VeiculoService veiculoService) {
        this.veiculoService = veiculoService;
    }

    public List<VeiculoDTO> listarTodos() {
        log.info("Controlador: Listando veículos ativos.");
        return veiculoService.listarAtivos();
    }

    public List<OrdensServicoDTO> buscarHistorico(String placa) {
        log.info("Controlador: Buscando histórico técnico para placa: {}", placa);
        return veiculoService.buscarHistorico(placa);
    }

    public VeiculoDTO salvar(VeiculoDTO veiculoDTO) {
        log.info("Controlador: Salvando veículo placa: {}", veiculoDTO.getPlaca());
        return veiculoService.salvar(veiculoDTO);
    }

    public void excluir(Long id) {
        log.warn("Controlador: Excluindo veículo ID: {}", id);
        veiculoService.excluirLogico(id);
    }
}
