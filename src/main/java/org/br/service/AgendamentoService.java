package org.br.service;

import lombok.extern.slf4j.Slf4j;
import org.br.interfaceDao.AgendamentoInterface;
import org.br.model.Agendamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoInterface agendamentoDAO;

    public List<Agendamento> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        log.info("Buscando agendamentos entre {} e {}", inicio, fim);
        return agendamentoDAO.findByDataHoraBetween(inicio, fim);
    }

    public Agendamento salvar(Agendamento agendamento) {
        log.info("Salvando agendamento para cliente ID: {}", agendamento.getCliente().getId());
        if (agendamento.getStatus() == null) {
            agendamento.setStatus(Agendamento.StatusAgendamento.AGENDADO);
        }
        return agendamentoDAO.save(agendamento);
    }
}
