package org.br.interfaceDao;

import org.br.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioInterface extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
}
