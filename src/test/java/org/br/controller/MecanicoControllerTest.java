package org.br.controller;

import org.br.dto.MecanicoDTO;
import org.br.service.MecanicoService;
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
public class MecanicoControllerTest {

    @Mock
    private MecanicoService mecanicoService;

    @InjectMocks
    private MecanicoController mecanicoController;

    @Test
    void listarTodos_DeveChamarService() {
        MecanicoDTO dto = MecanicoDTO.builder().nome("Carlos").build();
        when(mecanicoService.listarTodos()).thenReturn(Collections.singletonList(dto));

        List<MecanicoDTO> resultado = mecanicoController.listarTodos();

        assertEquals(1, resultado.size());
        assertEquals("Carlos", resultado.get(0).getNome());
        verify(mecanicoService, times(1)).listarTodos();
    }

    @Test
    void salvar_DeveChamarService() {
        MecanicoDTO dto = MecanicoDTO.builder().nome("Carlos").build();
        mecanicoController.salvar(dto);
        verify(mecanicoService, times(1)).salvar(dto);
    }
}
