package org.br.interfaceDao;

import org.br.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ClienteInterface extends JpaRepository<Cliente, Long> {
    List<Cliente> findByAtivoTrue();
}
