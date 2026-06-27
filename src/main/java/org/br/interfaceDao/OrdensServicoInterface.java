package org.br.interfaceDao;

import org.br.model.OrdensServico;
import org.br.model.StatusOS;
import org.br.model.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrdensServicoInterface extends JpaRepository<OrdensServico, Long> {
    List<OrdensServico> findByVeiculo(Veiculo veiculo);
    
    long countByStatusIn(List<StatusOS> statuses);
    
    long countByStatus(StatusOS status);

    @Query("SELECT COALESCE(SUM(o.valorTotalServicos + o.valorTotalPecas - o.desconto), 0.0) FROM OrdensServico o WHERE o.status = 'ENTREGUE'")
    Double sumFaturamentoEntregue();

    @Query("SELECT v FROM Veiculo v WHERE v.id IN (" +
           "SELECT o.veiculo.id FROM OrdensServico o " +
           "GROUP BY o.veiculo.id " +
           "HAVING MAX(o.dataEncerramento) < :dataLimite" +
           ")")
    List<Veiculo> findVeiculosParaLembreteRevisao(@Param("dataLimite") LocalDateTime dataLimite);
}
