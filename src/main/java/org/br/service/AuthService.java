package org.br.service;

import org.br.interfaceDao.UsuarioInterface;
import org.br.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UsuarioInterface usuarioDAO;

    private Usuario usuarioLogado;

    public boolean login(String username, String password) {
        Optional<Usuario> user = usuarioDAO.findByUsername(username);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
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
