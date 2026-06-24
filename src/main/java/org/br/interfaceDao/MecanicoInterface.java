package org.br.interfaceDao;

import org.br.model.Mecanico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MecanicoInterface extends JpaRepository<Mecanico, Long> {
}
