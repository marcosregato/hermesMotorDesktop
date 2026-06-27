package org.br.view;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.br.controller.*;
import org.br.model.ConfigOficina;
import org.br.model.Usuario;
import org.br.service.AuthService;
import org.br.utils.UIUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Getter
public class MainFrame extends JFrame {

    // --- Injeção de Dependências ---
    private final AuthService authService;
    private final ClienteController clienteController;
    private final MecanicoController mecanicoController;
    private final VeiculoController veiculoController;
    private final CatalogoController catalogoController;
    private final OrdemServicoController osController;
    private final DashboardController dashboardController;
    private final ConfigOficinaController configController;
    private final EstoqueController estoqueController;
    private final FinanceiroController financeiroController;
    private final AgendamentoController agendamentoController;
    private final NotificacaoController notificacaoController;

    // --- Componentes da UI ---
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JPanel sidebar;
    private JLabel lblLogo;
    private final Map<String, JButton> sidebarButtons = new HashMap<>();
    private final Map<String, Refreshable> refreshablePanels = new HashMap<>();

    // --- Painéis da Aplicação ---
    private ClienteListPanel clienteListPanel;
    private ClienteFormPanel clienteFormPanel;
    private MecanicoListPanel mecanicoListPanel;
    private MecanicoFormPanel mecanicoFormPanel;
    private VeiculoListPanel veiculoListPanel;
    private VeiculoFormPanel veiculoFormPanel;
    private CatalogoPanel catalogoPanel;
    private OSListPanel osListPanel;
    private OSFormPanel osFormPanel;
    private OSDetalhePanel osDetalhePanel;
    private VeiculoHistoricoPanel veiculoHistoricoPanel;
    private DashboardPanel dashboardPanel;
    private ConfigOficinaPanel configOficinaPanel;
    private EstoquePanel estoquePanel;
    private FinanceiroPanel financeiroPanel;
    private AgendaPanel agendaPanel;
    private LembretesPanel lembretesPanel;

    @Autowired
    public MainFrame(AuthService authService, ClienteController clienteController, MecanicoController mecanicoController,
                     VeiculoController veiculoController, CatalogoController catalogoController,
                     OrdemServicoController osController, DashboardController dashboardController,
                     ConfigOficinaController configController, EstoqueController estoqueController,
                     FinanceiroController financeiroController, AgendamentoController agendamentoController,
                     NotificacaoController notificacaoController) {

        // Atribuição das dependências
        this.authService = authService;
        this.clienteController = clienteController;
        this.mecanicoController = mecanicoController;
        this.veiculoController = veiculoController;
        this.catalogoController = catalogoController;
        this.osController = osController;
        this.dashboardController = dashboardController;
        this.configController = configController;
        this.estoqueController = estoqueController;
        this.financeiroController = financeiroController;
        this.agendamentoController = agendamentoController;
        this.notificacaoController = notificacaoController;

        // Configuração da Janela Principal
        setTitle("Hermes Motor Sport");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 850);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Inicialização dos componentes da UI
        initSidebar();
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(UIUtils.COLOR_BACKGROUND);
        initComponents();

        // Adiciona os painéis principais
        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // Aplica as permissões e mostra a tela inicial
        applyRolePermissions();
        showPanel("dashboard");
    }

    private void initSidebar() {
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setBackground(UIUtils.COLOR_SIDEBAR);
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(220, 220, 220)));

        lblLogo = new JLabel("HERMES MOTOR", JLabel.CENTER);
        lblLogo.setFont(new Font("sans-serif", Font.BOLD, 20));
        lblLogo.setForeground(new Color(30, 30, 30));
        lblLogo.setBorder(BorderFactory.createEmptyBorder(40, 10, 50, 10));
        lblLogo.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        sidebar.add(lblLogo);

        addSidebarButton("📊  Dashboard", "dashboard");
        addSidebarButton("📅  Agenda", "agenda");
        addSidebarButton("🔔  Lembretes", "lembretes");
        addSidebarButton("💰  Financeiro", "financeiro");
        addSidebarButton("📋  Ordens de Serviço", "osList");
        addSidebarButton("📦  Estoque", "estoque");
        addSidebarButton("👥  Clientes", "clienteList");
        addSidebarButton("🏍️  Veículos", "veiculoList");
        addSidebarButton("🔧  Mecânicos", "mecanicoList");
        addSidebarButton("📚  Catálogo", "catalogo");
        addSidebarButton("🔍  Histórico Técnico", "veiculoHistorico");

        sidebar.add(Box.createVerticalGlue());
        addSidebarButton("⚙️  Configurações", "configOficina");
        sidebar.add(Box.createVerticalStrut(30));
    }

    private void addSidebarButton(String text, String panelName) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(210, 45));
        btn.setFont(new Font("sans-serif", Font.PLAIN, 15));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        btn.setMargin(new Insets(0, 15, 0, 0));
        btn.addActionListener(e -> showPanel(panelName));
        sidebarButtons.put(panelName, btn);
        sidebar.add(btn);
        sidebar.add(Box.createVerticalStrut(5));
    }

    private void initComponents() {
        // Instanciação dos painéis
        clienteListPanel = new ClienteListPanel(clienteController, this);
        clienteFormPanel = new ClienteFormPanel(clienteController, this);
        mecanicoListPanel = new MecanicoListPanel(mecanicoController, this);
        mecanicoFormPanel = new MecanicoFormPanel(mecanicoController, this);
        veiculoListPanel = new VeiculoListPanel(veiculoController, this);
        veiculoFormPanel = new VeiculoFormPanel(veiculoController, clienteController, this);
        catalogoPanel = new CatalogoPanel(catalogoController);
        osListPanel = new OSListPanel(osController, this);
        osFormPanel = new OSFormPanel(osController, veiculoController, mecanicoController, this);
        osDetalhePanel = new OSDetalhePanel(osController, catalogoController, configController, this);
        veiculoHistoricoPanel = new VeiculoHistoricoPanel(veiculoController, this);
        dashboardPanel = new DashboardPanel(dashboardController);
        configOficinaPanel = new ConfigOficinaPanel(configController);
        estoquePanel = new EstoquePanel(estoqueController, catalogoController);
        financeiroPanel = new FinanceiroPanel(financeiroController);
        agendaPanel = new AgendaPanel(agendamentoController, clienteController, this);
        lembretesPanel = new LembretesPanel(notificacaoController);

        // Adição dos painéis ao CardLayout
        contentPanel.add(dashboardPanel, "dashboard");
        contentPanel.add(agendaPanel, "agenda");
        contentPanel.add(lembretesPanel, "lembretes");
        contentPanel.add(financeiroPanel, "financeiro");
        contentPanel.add(osListPanel, "osList");
        contentPanel.add(estoquePanel, "estoque");
        contentPanel.add(clienteListPanel, "clienteList");
        contentPanel.add(veiculoListPanel, "veiculoList");
        contentPanel.add(mecanicoListPanel, "mecanicoList");
        contentPanel.add(catalogoPanel, "catalogo");
        contentPanel.add(veiculoHistoricoPanel, "veiculoHistorico");
        contentPanel.add(configOficinaPanel, "configOficina");
        contentPanel.add(clienteFormPanel, "clienteForm");
        contentPanel.add(mecanicoFormPanel, "mecanicoForm");
        contentPanel.add(veiculoFormPanel, "veiculoForm");
        contentPanel.add(osFormPanel, "osForm");
        contentPanel.add(osDetalhePanel, "osDetalhe");

        // Registro dos painéis que podem ser atualizados
        refreshablePanels.put("dashboard", dashboardPanel);
        refreshablePanels.put("agenda", agendaPanel);
        refreshablePanels.put("lembretes", lembretesPanel);
        refreshablePanels.put("financeiro", financeiroPanel);
        refreshablePanels.put("osList", osListPanel);
        refreshablePanels.put("estoque", estoquePanel);
        refreshablePanels.put("clienteList", clienteListPanel);
        refreshablePanels.put("veiculoList", veiculoListPanel);
        refreshablePanels.put("mecanicoList", mecanicoListPanel);
        refreshablePanels.put("catalogo", catalogoPanel);
        refreshablePanels.put("veiculoHistorico", veiculoHistoricoPanel);
        refreshablePanels.put("configOficina", configOficinaPanel);
        refreshablePanels.put("clienteForm", clienteFormPanel);
        refreshablePanels.put("mecanicoForm", mecanicoFormPanel);
        refreshablePanels.put("veiculoForm", veiculoFormPanel);
        refreshablePanels.put("osForm", osFormPanel);
        refreshablePanels.put("osDetalhe", osDetalhePanel);
    }

    private void applyRolePermissions() {
        Usuario usuarioLogado = authService.getUsuarioLogado();
        if (usuarioLogado != null && usuarioLogado.getRole() == Usuario.Role.MECANICO) {
            log.info("Aplicando permissões para o perfil MECANICO.");
            List<String> menusBloqueados = Arrays.asList("financeiro", "estoque", "configOficina", "lembretes", "agenda");
            sidebarButtons.forEach((nome, botao) -> {
                if (menusBloqueados.contains(nome)) {
                    botao.setVisible(false);
                }
            });
        }
    }

    public void updateLogo() {
        ConfigOficina config = configController.getConfig();
        String path = config.getLogoPath();
        if (path != null && new File(path).exists()) {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage().getScaledInstance(150, -1, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(img));
            lblLogo.setText("");
        } else {
            lblLogo.setIcon(null);
            lblLogo.setText("HERMES MOTOR");
        }
    }

    public void showPanel(String name) {
        updateLogo();
        sidebarButtons.forEach((key, btn) -> {
            if (key.equals(name)) {
                btn.setContentAreaFilled(true);
                btn.setBackground(UIUtils.COLOR_PRIMARY);
                btn.setForeground(Color.WHITE);
                btn.setFont(new Font("sans-serif", Font.BOLD, 15));
            } else {
                btn.setContentAreaFilled(false);
                btn.setForeground(new Color(60, 60, 60));
                btn.setFont(new Font("sans-serif", Font.PLAIN, 15));
            }
        });

        Refreshable panel = refreshablePanels.get(name);
        if (panel != null) {
            panel.refresh();
        }
        cardLayout.show(contentPanel, name);
    }
}