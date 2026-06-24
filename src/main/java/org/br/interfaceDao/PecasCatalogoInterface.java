package org.br.interfaceDao;

import org.br.model.PecasCatalogo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PecasCatalogoInterface extends JpaRepository<PecasCatalogo, Long> {
    @Query("SELECT p FROM PecasCatalogo p WHERE p.quantidadeEstoque < 5")
    List<PecasCatalogo> findEstoqueBaixo();
}
