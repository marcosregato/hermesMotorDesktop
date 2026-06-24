package org.br.interfaceDao;

import org.br.model.EstoqueMovimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstoqueMovimentacaoInterface extends JpaRepository<EstoqueMovimentacao, Long> {
}
