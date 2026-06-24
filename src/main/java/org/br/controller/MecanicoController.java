package org.br.controller;

import org.br.dto.MecanicoDTO;
import org.br.service.MecanicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MecanicoController {

    @Autowired
    private MecanicoService mecanicoService;

    public List<MecanicoDTO> listarTodos() {
        return mecanicoService.listarTodos();
    }

    public MecanicoDTO salvar(MecanicoDTO mecanicoDTO) {
        return mecanicoService.salvar(mecanicoDTO);
    }

    public void excluir(Long id) {
        mecanicoService.excluir(id);
    }
}
