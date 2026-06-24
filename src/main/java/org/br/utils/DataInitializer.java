package org.br.utils;

import org.br.interfaceDao.*;
import org.br.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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

    @Override
    public void run(String... args) throws Exception {
        // 0. Usuários
        if (usuarioDAO.count() == 0) {
            usuarioDAO.save(Usuario.builder().username("admin").password("admin").role(Usuario.Role.ADMIN).build());
            usuarioDAO.save(Usuario.builder().username("mecanico").password("123").role(Usuario.Role.MECANICO).build());
        }

        // 1. Configuração da Oficina
        if (configDAO.count() == 0) {
            configDAO.save(ConfigOficina.builder()
                    .id(1L)
                    .nomeOficina("Hermes Motor Sport")
                    .cnpj("12.345.678/0001-99")
                    .telefone("(11) 98888-7777")
                    .endereco("Rua das Motos, 100 - São Paulo")
                    .mensagemPadraoWhatsapp("Olá! A manutenção da sua moto foi concluída com sucesso pela equipe Hermes Motor.")
                    .build());
        }

        // 2. Clientes
        if (clienteDAO.count() == 0) {
            Cliente c1 = clienteDAO.save(Cliente.builder().nome("João Silva").cpfCnpj("123.456.789-00").telefone("(11) 91234-5678").email("joao@email.com").build());
            Cliente c2 = clienteDAO.save(Cliente.builder().nome("Maria Oliveira").cpfCnpj("987.654.321-11").telefone("(11) 99876-5432").email("maria@email.com").build());

            // 3. Veículos
            Veiculo v1 = veiculoDAO.save(Veiculo.builder().cliente(c1).placa("HON1234").marca("Honda").modelo("CB 500F").anoModelo(2022).cor("Vermelha").build());
            Veiculo v2 = veiculoDAO.save(Veiculo.builder().cliente(c2).placa("YAM5566").marca("Yamaha").modelo("MT-07").anoModelo(2021).cor("Azul").build());

            // 4. Mecânicos
            Mecanico m1 = mecanicoDAO.save(Mecanico.builder().nome("Mestre Silva").cpf("111.222.333-44").especialidade("Motores").ativo(true).build());

            // 5. Catálogo
            servicosDAO.save(ServicosCatalogo.builder().descricao("Troca de Óleo").precoBase(new BigDecimal("50.00")).build());
            pecasDAO.save(PecasCatalogo.builder().codigoPeca("OL-MOTUL").nome("Óleo Motul 5100").precoVenda(new BigDecimal("65.00")).quantidadeEstoque(20).build());

            // 6. Algumas Ordens de Serviço
            osDAO.save(OrdensServico.builder().veiculo(v1).mecanico(m1).status(StatusOS.EM_EXECUCAO).relatoCliente("Barulho no freio").quilometragemEntrada(15000).nivelCombustivel("Meio").dataAbertura(LocalDateTime.now()).valorTotalServicos(BigDecimal.ZERO).valorTotalPecas(BigDecimal.ZERO).build());
        }

        System.out.println(">>> Banco H2 inicializado com usuários e dados de demonstração!");
    }
}
