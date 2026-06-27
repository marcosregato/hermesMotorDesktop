package org.br.interfaceDao;

import org.br.model.LancamentoFinanceiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LancamentoFinanceiroInterface extends JpaRepository<LancamentoFinanceiro, Long> {

    List<LancamentoFinanceiro> findByDataLancamentoBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT COALESCE(SUM(CASE WHEN l.tipo = 'RECEITA' THEN l.valor ELSE -l.valor END), 0.0) " +
           "FROM LancamentoFinanceiro l " +
           "WHERE l.dataPagamento IS NOT NULL AND l.dataPagamento >= :inicio AND l.dataPagamento < :fim")
    BigDecimal sumByDataPagamentoBetween(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    @Query("SELECT COALESCE(SUM(l.valor), 0.0) " +
           "FROM LancamentoFinanceiro l " +
           "WHERE l.tipo = 'RECEITA' AND l.dataPagamento IS NULL")
    BigDecimal sumContasAReceber();
}
