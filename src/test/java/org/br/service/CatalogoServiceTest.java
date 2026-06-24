package org.br.service;

import org.br.dto.PecasCatalogoDTO;
import org.br.interfaceDao.PecasCatalogoInterface;
import org.br.interfaceDao.ServicosCatalogoInterface;
import org.br.model.PecasCatalogo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CatalogoServiceTest {

    @Mock
    private ServicosCatalogoInterface servicosDAO;

    @Mock
    private PecasCatalogoInterface pecasDAO;

    @InjectMocks
    private CatalogoService catalogoService;

    @Test
    void listarPecas_DeveRetornarTodasAsPecas() {
        PecasCatalogo peca = PecasCatalogo.builder().id(1L).nome("Pneu").precoVenda(BigDecimal.TEN).build();
        when(pecasDAO.findAll()).thenReturn(Collections.singletonList(peca));

        List<PecasCatalogoDTO> resultado = catalogoService.listarPecas();

        assertEquals(1, resultado.size());
        assertEquals("Pneu", resultado.get(0).getNome());
    }

    @Test
    void salvarPeca_DevePersistirPecaNoBanco() {
        PecasCatalogoDTO dto = PecasCatalogoDTO.builder().nome("Corrente").precoVenda(BigDecimal.ONE).build();
        
        catalogoService.salvarPeca(dto);

        verify(pecasDAO, times(1)).save(any(PecasCatalogo.class));
    }
}
