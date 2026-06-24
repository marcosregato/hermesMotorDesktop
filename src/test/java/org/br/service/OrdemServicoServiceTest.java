package org.br.service;

import org.br.dto.OrdensServicoDTO;
import org.br.dto.OsItemDTO;
import org.br.interfaceDao.*;
import org.br.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrdemServicoServiceTest {

    @Mock private OrdensServicoInterface ordensServicoDAO;
    @Mock private PecasCatalogoInterface pecasDAO;
    @Mock private OsPecasInterface osPecasDAO;
    @Mock private OsServicosInterface osServicosDAO;
    @Mock private ServicosCatalogoInterface servicosDAO;
    @Mock private VeiculoInterface veiculoDAO; // Adicionado para toEntity
    @Mock private MecanicoInterface mecanicoDAO; // Adicionado para toEntity

    @InjectMocks
    private OrdemServicoService osService;

    private OrdensServico os;
    private PecasCatalogo peca;
    private ServicosCatalogo servico;

    @BeforeEach
    void setUp() {
        os = OrdensServico.builder().id(1L).status(StatusOS.ORCAMENTO).build();
        peca = PecasCatalogo.builder().id(1L).nome("Pneu").quantidadeEstoque(10).precoVenda(new BigDecimal("200.00")).build();
        servico = ServicosCatalogo.builder().id(2L).descricao("Troca de Oleo").precoBase(new BigDecimal("50.00")).build();
    }

    @Test
    void alterarStatus_DeveAtualizarStatusEDatamEncerramento() {
        when(ordensServicoDAO.findById(1L)).thenReturn(Optional.of(os));

        osService.alterarStatus(1L, StatusOS.ENTREGUE);

        assertEquals(StatusOS.ENTREGUE, os.getStatus());
        assertNotNull(os.getDataEncerramento());
        verify(ordensServicoDAO, times(1)).save(os);
    }

    @Test
    void adicionarPeca_DeveLancarExcecaoQuandoSemEstoque() {
        peca.setQuantidadeEstoque(2); // Reduz estoque para forçar erro
        when(ordensServicoDAO.findById(1L)).thenReturn(Optional.of(os));
        when(pecasDAO.findById(1L)).thenReturn(Optional.of(peca));

        assertThrows(RuntimeException.class, () -> {
            osService.adicionarPeca(1L, 1L, 10);
        });
    }

    @Test
    void removerItem_Peca_DeveDevolverAoEstoque() {
        OsPecas osPeca = OsPecas.builder().id(100L).ordemServico(os).peca(peca).quantidade(2).build();
        when(osPecasDAO.findById(100L)).thenReturn(Optional.of(osPeca));
        when(osServicosDAO.findByOrdemServico(os)).thenReturn(Collections.emptyList()); // Para atualizar totais
        when(osPecasDAO.findByOrdemServico(os)).thenReturn(Collections.emptyList()); // Para atualizar totais

        osService.removerItem(100L, "PECA");

        assertEquals(12, peca.getQuantidadeEstoque()); // 10 inicial + 2 devolvidos
        verify(pecasDAO).save(peca);
        verify(osPecasDAO).deleteById(100L);
        verify(ordensServicoDAO).save(os); // Verifica se os totais foram atualizados
    }

    @Test
    void removerItem_Servico_DeveRemoverESalvarOS() {
        OsServicos osServico = OsServicos.builder().id(101L).ordemServico(os).servico(servico).quantidade(1).build();
        when(osServicosDAO.findById(101L)).thenReturn(Optional.of(osServico));
        when(osServicosDAO.findByOrdemServico(os)).thenReturn(Collections.emptyList()); // Para atualizar totais
        when(osPecasDAO.findByOrdemServico(os)).thenReturn(Collections.emptyList()); // Para atualizar totais

        osService.removerItem(101L, "SERVICO");

        verify(osServicosDAO).deleteById(101L);
        verify(ordensServicoDAO).save(os); // Verifica se os totais foram atualizados
    }

    @Test
    void listarItensDaOS_DeveRetornarListaUnificada() {
        OsPecas osPeca = OsPecas.builder().id(100L).ordemServico(os).peca(peca).quantidade(1).valorUnitario(peca.getPrecoVenda()).build();
        OsServicos osServico = OsServicos.builder().id(101L).ordemServico(os).servico(servico).quantidade(1).valorUnitario(servico.getPrecoBase()).build();

        when(ordensServicoDAO.findById(1L)).thenReturn(Optional.of(os));
        when(osPecasDAO.findByOrdemServico(os)).thenReturn(Collections.singletonList(osPeca));
        when(osServicosDAO.findByOrdemServico(os)).thenReturn(Collections.singletonList(osServico));

        List<OsItemDTO> itens = osService.listarItensDaOS(1L);

        assertEquals(2, itens.size());
        assertTrue(itens.stream().anyMatch(item -> item.getTipo().equals("PECA")));
        assertTrue(itens.stream().anyMatch(item -> item.getTipo().equals("SERVICO")));
    }

    @Test
    void adicionarServico_DeveAdicionarESalvarOS() {
        when(ordensServicoDAO.findById(1L)).thenReturn(Optional.of(os));
        when(servicosDAO.findById(2L)).thenReturn(Optional.of(servico));
        when(osServicosDAO.findByOrdemServico(os)).thenReturn(Collections.singletonList(OsServicos.builder().quantidade(1).valorUnitario(BigDecimal.TEN).build())); // Mock para atualizar totais
        when(osPecasDAO.findByOrdemServico(os)).thenReturn(Collections.emptyList());

        osService.adicionarServico(1L, 2L, 1);

        verify(osServicosDAO).save(any(OsServicos.class));
        verify(ordensServicoDAO).save(os); // Verifica se os totais foram atualizados
    }

    @Test
    void salvar_DeveCriarNovaOSComValoresIniciais() {
        OrdensServicoDTO dto = OrdensServicoDTO.builder().idVeiculo(1L).build();
        Veiculo veiculo = Veiculo.builder().id(1L).build();
        when(veiculoDAO.findById(1L)).thenReturn(Optional.of(veiculo));
        when(ordensServicoDAO.save(any(OrdensServico.class))).thenAnswer(invocation -> {
            OrdensServico savedOs = invocation.getArgument(0);
            savedOs.setId(1L); // Simula o ID gerado pelo banco
            return savedOs;
        });

        OrdensServicoDTO result = osService.salvar(dto);

        assertNotNull(result.getId());
        assertEquals(StatusOS.ORCAMENTO, result.getStatus());
        assertEquals(BigDecimal.ZERO, result.getValorTotalServicos());
        verify(ordensServicoDAO).save(any(OrdensServico.class));
    }
}
