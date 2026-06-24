package org.br.controller;

import org.br.dto.ClienteDTO;
import org.br.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteControllerTest {

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    @Test
    void listarTodos_DeveChamarServiceERetornarLista() {
        ClienteDTO dto = ClienteDTO.builder().id(1L).nome("João").build();
        when(clienteService.listarAtivos()).thenReturn(Collections.singletonList(dto));

        List<ClienteDTO> resultado = clienteController.listarTodos();

        assertEquals(1, resultado.size());
        assertEquals("João", resultado.get(0).getNome());
        verify(clienteService, times(1)).listarAtivos();
    }

    @Test
    void salvar_DeveChamarService() {
        ClienteDTO dto = ClienteDTO.builder().nome("João").build();
        clienteController.salvar(dto);
        verify(clienteService, times(1)).salvar(dto);
    }

    @Test
    void excluir_DeveChamarService() {
        clienteController.excluir(1L);
        verify(clienteService, times(1)).excluirLogico(1L);
    }
}
