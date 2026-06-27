package org.br.utils;

import org.br.interfaceDao.*;
import org.br.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private ClienteInterface clienteDAO;
    @Autowired private VeiculoInterface veiculoDAO;
    @Autowired private MecanicoInterface mecanicoDAO;
    @Autowired private ServicosCatalogoInterface servicosDAO;
    @Autowired private PecasCatalogoInterface pecasDAO;
    @Autowired private OrdensServicoInterface osDAO;
    @Autowired private ConfigOficinaInterface configDAO;
    @Autowired private UsuarioInterface usuarioDAO;
    @Autowired private LancamentoFinanceiroInterface lancamentoDAO;
    @Autowired private EstoqueMovimentacaoInterface estoqueMovimentacaoDAO;
    @Autowired private OsPecasInterface osPecasDAO;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (usuarioDAO.count() > 0) return;

        // Usuários com senhas codificadas
        usuarioDAO.save(Usuario.builder().username("admin").password(passwordEncoder.encode("admin")).role(Usuario.Role.ADMIN).build());
        usuarioDAO.save(Usuario.builder().username("mecanico").password(passwordEncoder.encode("123")).role(Usuario.Role.MECANICO).build());

        // Configuração
        configDAO.save(ConfigOficina.builder().id(1L).nomeOficina("Hermes Motor Sport").build());

        // Clientes
        Cliente c1 = clienteDAO.save(Cliente.builder().nome("João da Silva").telefone("11987654321").cpfCnpj("12345678900").build());
        Cliente c2 = clienteDAO.save(Cliente.builder().nome("Maria Oliveira").telefone("21998765432").cpfCnpj("98765432100").build());

        // Veículos
        Veiculo v1 = veiculoDAO.save(Veiculo.builder().cliente(c1).placa("ABC1234").marca("Honda").modelo("CB 500").anoFabricacao(2020).anoModelo(2021).build());
        Veiculo v2 = veiculoDAO.save(Veiculo.builder().cliente(c2).placa("XYZ5678").marca("Yamaha").modelo("MT-07").anoFabricacao(2022).anoModelo(2022).build());

        // Mecânico
        Mecanico m1 = mecanicoDAO.save(Mecanico.builder().nome("Mestre Carlos").cpf("11122233344").build());

        // Catálogo
        servicosDAO.save(ServicosCatalogo.builder().descricao("Troca de Óleo").precoBase(new BigDecimal("50.00")).build());
        PecasCatalogo oleo = pecasDAO.save(PecasCatalogo.builder().nome("Óleo Motul").precoVenda(new BigDecimal("65.00")).quantidadeEstoque(10).build());

        // O.S. Antiga
        OrdensServico osAntiga = osDAO.save(OrdensServico.builder()
                .veiculo(v1)
                .mecanico(m1)
                .status(StatusOS.ENTREGUE)
                .relatoCliente("Moto falhando em baixa rotação.")
                .dataEncerramento(LocalDateTime.now().minusMonths(7))
                .valorTotalServicos(new BigDecimal("50.00"))
                .valorTotalPecas(BigDecimal.ZERO)
                .desconto(BigDecimal.ZERO)
                .build());
        
        // Lançamento para OS Antiga
        lancamentoDAO.save(LancamentoFinanceiro.builder()
                .descricao("Receita da O.S. #" + osAntiga.getId())
                .tipo(LancamentoFinanceiro.TipoLancamento.RECEITA)
                .valor(osAntiga.getValorGeralTotal())
                .ordemServico(osAntiga)
                .dataVencimento(LocalDate.now().minusMonths(7))
                .dataPagamento(LocalDateTime.now().minusMonths(7))
                .build());

        // O.S. Recente
        OrdensServico osRecente = osDAO.save(OrdensServico.builder()
                .veiculo(v2)
                .mecanico(m1)
                .status(StatusOS.EM_EXECUCAO)
                .relatoCliente("Verificar barulho no freio dianteiro.")
                .dataAbertura(LocalDateTime.now())
                .valorTotalServicos(BigDecimal.ZERO)
                .valorTotalPecas(BigDecimal.ZERO)
                .desconto(BigDecimal.ZERO)
                .build());
        
        // Adiciona peça à OS Recente e gera movimentação
        osPecasDAO.save(OsPecas.builder()
                .ordemServico(osRecente)
                .peca(oleo)
                .quantidade(1)
                .valorUnitario(oleo.getPrecoVenda())
                .build());

        estoqueMovimentacaoDAO.save(EstoqueMovimentacao.builder()
                .peca(oleo)
                .tipo(EstoqueMovimentacao.TipoMovimentacao.SAIDA_OS)
                .quantidade(1)
                .observacao("Saída para OS #" + osRecente.getId())
                .build());
        
        oleo.setQuantidadeEstoque(oleo.getQuantidadeEstoque() - 1);
        pecasDAO.save(oleo);

        // Lançamento de Despesa
        lancamentoDAO.save(LancamentoFinanceiro.builder()
                .descricao("Aluguel da Oficina")
                .tipo(LancamentoFinanceiro.TipoLancamento.DESPESA)
                .valor(new BigDecimal("1500.00"))
                .dataVencimento(LocalDate.now().withDayOfMonth(5))
                .build());

        System.out.println(">>> Banco H2 inicializado com dados de demonstração!");
    }
}
