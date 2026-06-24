package org.br.controller;

import org.br.model.Veiculo;
import org.br.service.NotificacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificacaoController {

    @Autowired
    private NotificacaoService notificacaoService;

    public List<Veiculo> getVeiculosParaLembrete() {
        return notificacaoService.getVeiculosParaLembrete();
    }
}
