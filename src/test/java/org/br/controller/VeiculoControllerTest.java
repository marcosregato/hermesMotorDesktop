package org.br.controller;

import org.br.dto.VeiculoDTO;
import org.br.service.VeiculoService;
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
public class VeiculoControllerTest {

    @Mock
    private VeiculoService veiculoService;

    @InjectMocks
    private VeiculoController veiculoController;

    @Test
    void listarTodos_DeveChamarService() {
        VeiculoDTO dto = VeiculoDTO.builder().placa("ABC1234").build();
        when(veiculoService.listarAtivos()).thenReturn(Collections.singletonList(dto));

        List<VeiculoDTO> resultado = veiculoController.listarTodos();

        assertEquals(1, resultado.size());
        assertEquals("ABC1234", resultado.get(0).getPlaca());
        verify(veiculoService, times(1)).listarAtivos();
    }

    @Test
    void salvar_DeveChamarService() {
        VeiculoDTO dto = VeiculoDTO.builder().placa("XYZ5566").build();
        veiculoController.salvar(dto);
        verify(veiculoService, times(1)).salvar(dto);
    }

    @Test
    void excluir_DeveChamarService() {
        veiculoController.excluir(1L);
        verify(veiculoService, times(1)).excluirLogico(1L);
    }
}
