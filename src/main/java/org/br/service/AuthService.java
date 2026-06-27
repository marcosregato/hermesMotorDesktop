package org.br.service;

import org.br.interfaceDao.UsuarioInterface;
import org.br.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UsuarioInterface usuarioDAO;
    private final PasswordEncoder passwordEncoder;
    private Usuario usuarioLogado;

    @Autowired
    public AuthService(UsuarioInterface usuarioDAO, PasswordEncoder passwordEncoder) {
        this.usuarioDAO = usuarioDAO;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean login(String username, String password) {
        Optional<Usuario> user = usuarioDAO.findByUsername(username);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            this.usuarioLogado = user.get();
            return true;
        }
        return false;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public void logout() {
        this.usuarioLogado = null;
    }

    public boolean isAdmin() {
        return usuarioLogado != null && usuarioLogado.getRole() == Usuario.Role.ADMIN;
    }
}
