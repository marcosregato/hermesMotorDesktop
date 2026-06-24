package org.br.controller;

import org.br.dto.PecasCatalogoDTO;
import org.br.dto.ServicosCatalogoDTO;
import org.br.service.CatalogoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CatalogoController {

    @Autowired
    private CatalogoService catalogoService;

    public List<ServicosCatalogoDTO> listarServicos() {
        return catalogoService.listarServicos();
    }

    public void salvarServico(ServicosCatalogoDTO dto) {
        catalogoService.salvarServico(dto);
    }

    public List<PecasCatalogoDTO> listarPecas() {
        return catalogoService.listarPecas();
    }

    public void salvarPeca(PecasCatalogoDTO dto) {
        catalogoService.salvarPeca(dto);
    }
}
