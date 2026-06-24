package org.br.service;

import org.br.dto.ClienteDTO;
import org.br.interfaceDao.ClienteInterface;
import org.br.model.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @Mock
    private ClienteInterface clienteDAO;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;
    private ClienteDTO clienteDTO;

    @BeforeEach
    void setUp() {
        cliente = Cliente.builder()
                .id(1L)
                .nome("João Teste")
                .cpfCnpj("123.456.789-00")
                .ativo(true)
                .build();

        clienteDTO = ClienteDTO.builder()
                .id(1L)
                .nome("João Teste")
                .cpfCnpj("123.456.789-00")
                .build();
    }

    @Test
    void listarAtivos_DeveRetornarListaDeClientes() {
        when(clienteDAO.findByAtivoTrue()).thenReturn(Collections.singletonList(cliente));

        List<ClienteDTO> resultado = clienteService.listarAtivos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("João Teste", resultado.get(0).getNome());
        verify(clienteDAO, times(1)).findByAtivoTrue();
    }

    @Test
    void salvar_DevePersistirCliente() {
        when(clienteDAO.save(any(Cliente.class))).thenReturn(cliente);

        ClienteDTO salvo = clienteService.salvar(clienteDTO);

        assertNotNull(salvo);
        assertEquals("João Teste", salvo.getNome());
        verify(clienteDAO, times(1)).save(any(Cliente.class));
    }

    @Test
    void excluirLogico_DeveDesativarCliente() {
        when(clienteDAO.findById(1L)).thenReturn(Optional.of(cliente));

        clienteService.excluirLogico(1L);

        assertFalse(cliente.getAtivo());
        verify(clienteDAO, times(1)).save(cliente);
    }
}
