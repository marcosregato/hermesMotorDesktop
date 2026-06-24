package org.br.interfaceDao;

import org.br.model.ConfigOficina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigOficinaInterface extends JpaRepository<ConfigOficina, Long> {
}
