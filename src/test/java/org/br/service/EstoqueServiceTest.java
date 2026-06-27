package org.br.service;

import org.br.interfaceDao.EstoqueMovimentacaoInterface;
import org.br.interfaceDao.PecasCatalogoInterface;
import org.br.model.EstoqueMovimentacao;
import org.br.model.PecasCatalogo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EstoqueServiceTest {

    @Mock
    private PecasCatalogoInterface pecasDAO;

    @Mock
    private EstoqueMovimentacaoInterface movimentacaoDAO;

    @InjectMocks
    private EstoqueService estoqueService;

    private PecasCatalogo peca;

    @BeforeEach
    void setUp() {
        peca = PecasCatalogo.builder()
                .id(1L)
                .nome("Filtro de Ar")
                .quantidadeEstoque(10)
                .build();
    }

    @Test
    void registrarEntrada_DeveAumentarEstoqueESalvarMovimentacao() {
        when(pecasDAO.findById(1L)).thenReturn(Optional.of(peca));

        estoqueService.registrarEntrada(1L, 5, "NF-e 123");

        assertEquals(15, peca.getQuantidadeEstoque());
        verify(pecasDAO).save(peca);
        verify(movimentacaoDAO).save(any(EstoqueMovimentacao.class));
    }

    @Test
    void registrarSaida_DeveDiminuirEstoqueESalvarMovimentacao() {
        when(pecasDAO.findById(1L)).thenReturn(Optional.of(peca));

        estoqueService.registrarSaida(1L, 3, "OS #1");

        assertEquals(7, peca.getQuantidadeEstoque());
        verify(pecasDAO).save(peca);
        verify(movimentacaoDAO).save(any(EstoqueMovimentacao.class));
    }

    @Test
    void registrarSaida_DeveLancarExcecaoSeEstoqueInsuficiente() {
        when(pecasDAO.findById(1L)).thenReturn(Optional.of(peca));

        assertThrows(RuntimeException.class, () -> {
            estoqueService.registrarSaida(1L, 20, "OS #2");
        });
    }
}
