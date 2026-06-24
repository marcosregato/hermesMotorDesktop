package org.br.interfaceDao;

import org.br.model.OrdensServico;
import org.br.model.OsServicos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OsServicosInterface extends JpaRepository<OsServicos, Long> {
    List<OsServicos> findByOrdemServico(OrdensServico os);
}
