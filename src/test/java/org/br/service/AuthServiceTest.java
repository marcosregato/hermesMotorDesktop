package org.br.service;

import org.br.interfaceDao.UsuarioInterface;
import org.br.model.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UsuarioInterface usuarioDAO;

    @InjectMocks
    private AuthService authService;

    @Test
    void login_DeveRetornarTrueParaCredenciaisCorretas() {
        Usuario user = Usuario.builder().username("admin").password("admin").role(Usuario.Role.ADMIN).build();
        when(usuarioDAO.findByUsername("admin")).thenReturn(Optional.of(user));

        assertTrue(authService.login("admin", "admin"));
        assertNotNull(authService.getUsuarioLogado());
        assertEquals(Usuario.Role.ADMIN, authService.getUsuarioLogado().getRole());
    }

    @Test
    void login_DeveRetornarFalseParaSenhaIncorreta() {
        Usuario user = Usuario.builder().username("admin").password("admin").build();
        when(usuarioDAO.findByUsername("admin")).thenReturn(Optional.of(user));

        assertFalse(authService.login("admin", "senha_errada"));
        assertNull(authService.getUsuarioLogado());
    }

    @Test
    void isAdmin_DeveFuncionarCorretamente() {
        Usuario admin = Usuario.builder().username("admin").password("admin").role(Usuario.Role.ADMIN).build();
        when(usuarioDAO.findByUsername("admin")).thenReturn(Optional.of(admin));
        authService.login("admin", "admin");
        assertTrue(authService.isAdmin());

        authService.logout();

        Usuario mecanico = Usuario.builder().username("mecanico").password("123").role(Usuario.Role.MECANICO).build();
        when(usuarioDAO.findByUsername("mecanico")).thenReturn(Optional.of(mecanico));
        authService.login("mecanico", "123");
        assertFalse(authService.isAdmin());
    }
}
