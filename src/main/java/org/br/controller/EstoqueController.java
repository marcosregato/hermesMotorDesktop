package org.br.controller;

import org.br.dto.EstoqueMovimentacaoDTO;
import org.br.service.EstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EstoqueController {

    @Autowired
    private EstoqueService estoqueService;

    public List<EstoqueMovimentacaoDTO> listarMovimentacoes() {
        return estoqueService.listarMovimentacoes();
    }

    public void registrarEntrada(Long idPeca, int quantidade, String observacao) {
        estoqueService.registrarEntrada(idPeca, quantidade, observacao);
    }
}
