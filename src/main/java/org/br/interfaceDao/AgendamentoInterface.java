package org.br.interfaceDao;

import org.br.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AgendamentoInterface extends JpaRepository<Agendamento, Long> {
    List<Agendamento> findByDataHoraBetween(LocalDateTime start, LocalDateTime end);
}
