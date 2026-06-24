package org.br.interfaceDao;

import org.br.model.LancamentoFinanceiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LancamentoFinanceiroInterface extends JpaRepository<LancamentoFinanceiro, Long> {
}
