package org.br.interfaceDao;

import org.br.model.ServicosCatalogo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicosCatalogoInterface extends JpaRepository<ServicosCatalogo, Long> {
}
