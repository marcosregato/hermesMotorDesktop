package org.br.controller;

import org.br.dto.PecasCatalogoDTO;
import org.br.dto.ServicosCatalogoDTO;
import org.br.service.CatalogoService;
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
public class CatalogoControllerTest {

    @Mock
    private CatalogoService catalogoService;

    @InjectMocks
    private CatalogoController catalogoController;

    @Test
    void listarServicos_DeveChamarService() {
        ServicosCatalogoDTO dto = ServicosCatalogoDTO.builder().descricao("Revisão").build();
        when(catalogoService.listarServicos()).thenReturn(Collections.singletonList(dto));

        List<ServicosCatalogoDTO> resultado = catalogoController.listarServicos();

        assertEquals(1, resultado.size());
        assertEquals("Revisão", resultado.get(0).getDescricao());
        verify(catalogoService, times(1)).listarServicos();
    }

    @Test
    void salvarPeca_DeveChamarService() {
        PecasCatalogoDTO dto = PecasCatalogoDTO.builder().nome("Filtro").build();
        catalogoController.salvarPeca(dto);
        verify(catalogoService, times(1)).salvarPeca(dto);
    }
}
