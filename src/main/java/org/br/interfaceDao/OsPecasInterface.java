package org.br.interfaceDao;

import org.br.model.OrdensServico;
import org.br.model.OsPecas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OsPecasInterface extends JpaRepository<OsPecas, Long> {
    List<OsPecas> findByOrdemServico(OrdensServico os);
}
