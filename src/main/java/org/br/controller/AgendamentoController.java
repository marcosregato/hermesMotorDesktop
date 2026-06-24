package org.br.controller;

import org.br.model.Agendamento;
import org.br.service.AgendamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class AgendamentoController {

    @Autowired
    private AgendamentoService agendamentoService;

    public List<Agendamento> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return agendamentoService.buscarPorPeriodo(inicio, fim);
    }

    public Agendamento salvar(Agendamento agendamento) {
        return agendamentoService.salvar(agendamento);
    }
}
