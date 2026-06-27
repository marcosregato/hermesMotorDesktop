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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT) // Tolera stubs desnecessários
public class OrdemServicoServiceTest {

    @Mock private OrdensServicoInterface ordensServicoDAO;
    @Mock private PecasCatalogoInterface pecasDAO;
    @Mock private OsPecasInterface osPecasDAO;
    @Mock private OsServicosInterface osServicosDAO;
    @Mock private ServicosCatalogoInterface servicosDAO;
    @Mock private VeiculoInterface veiculoDAO;
    @Mock private MecanicoInterface mecanicoDAO;
    
    // Mocks para os serviços injetados
    @Mock private EstoqueService estoqueService;
    @Mock private FinanceiroService financeiroService;

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
    void alterarStatus_DeveChamarFinanceiroService() {
        when(ordensServicoDAO.findById(1L)).thenReturn(Optional.of(os));

        osService.alterarStatus(1L, StatusOS.ENTREGUE);

        assertEquals(StatusOS.ENTREGUE, os.getStatus());
        assertNotNull(os.getDataEncerramento());
        verify(ordensServicoDAO).save(os);
        verify(financeiroService).criarReceitaDeOS(os); // Verifica a chamada
    }

    @Test
    void adicionarPeca_DeveChamarEstoqueService() {
        when(ordensServicoDAO.findById(1L)).thenReturn(Optional.of(os));
        when(pecasDAO.findById(1L)).thenReturn(Optional.of(peca));

        osService.adicionarPeca(1L, 1L, 1);

        verify(estoqueService).registrarSaida(1L, 1, "Saída para OS #1");
        verify(osPecasDAO).save(any(OsPecas.class));
    }

    @Test
    void removerItem_Peca_DeveChamarEstoqueService() {
        OsPecas osPeca = OsPecas.builder().id(100L).ordemServico(os).peca(peca).quantidade(2).build();
        when(osPecasDAO.findById(100L)).thenReturn(Optional.of(osPeca));

        osService.removerItem(100L, "PECA");

        verify(estoqueService).registrarEntrada(1L, 2, "Devolução da OS #1");
        verify(osPecasDAO).deleteById(100L);
    }
    
    // ... (demais testes permanecem válidos)
}
