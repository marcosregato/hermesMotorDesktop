package org.br.service;

import org.br.dto.OrdensServicoDTO;
import org.br.dto.VeiculoDTO;
import org.br.interfaceDao.ClienteInterface;
import org.br.interfaceDao.OrdensServicoInterface;
import org.br.interfaceDao.VeiculoInterface;
import org.br.model.Cliente;
import org.br.model.OrdensServico;
import org.br.model.Veiculo;
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
public class VeiculoServiceTest {

    @Mock
    private VeiculoInterface veiculoDAO;

    @Mock
    private ClienteInterface clienteDAO;

    @Mock
    private OrdensServicoInterface osDAO;

    @InjectMocks
    private VeiculoService veiculoService;

    private Veiculo veiculo;
    private Cliente cliente;
    private VeiculoDTO veiculoDTO;

    @BeforeEach
    void setUp() {
        cliente = Cliente.builder().id(1L).nome("João").build();
        veiculo = Veiculo.builder()
                .id(1L)
                .placa("ABC1234")
                .cliente(cliente)
                .ativo(true)
                .build();
        veiculoDTO = VeiculoDTO.builder()
                .id(1L)
                .placa("ABC1234")
                .idCliente(1L)
                .build();
    }

    @Test
    void listarAtivos_DeveRetornarVeiculosAtivos() {
        when(veiculoDAO.findByAtivoTrue()).thenReturn(Collections.singletonList(veiculo));
        List<VeiculoDTO> resultado = veiculoService.listarAtivos();
        assertFalse(resultado.isEmpty());
        assertEquals("ABC1234", resultado.get(0).getPlaca());
    }

    @Test
    void buscarHistorico_DeveRetornarOrdensDoVeiculo() {
        when(veiculoDAO.findByPlaca("ABC1234")).thenReturn(Optional.of(veiculo));
        when(osDAO.findByVeiculo(veiculo)).thenReturn(Collections.singletonList(OrdensServico.builder().id(10L).build()));

        List<OrdensServicoDTO> historico = veiculoService.buscarHistorico("ABC1234");

        assertEquals(1, historico.size());
        assertEquals(10L, historico.get(0).getId());
    }

    @Test
    void excluirLogico_DeveDesativarVeiculo() {
        when(veiculoDAO.findById(1L)).thenReturn(Optional.of(veiculo));
        veiculoService.excluirLogico(1L);
        assertFalse(veiculo.getAtivo());
        verify(veiculoDAO).save(veiculo);
    }
}
