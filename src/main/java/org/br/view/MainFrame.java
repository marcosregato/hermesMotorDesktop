package org.br.view;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.br.controller.*;
import org.br.model.ConfigOficina;
import org.br.utils.UIUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@Getter
public class MainFrame extends JFrame {

    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JPanel sidebar;
    private JLabel lblLogo;
    private Map<String, JButton> sidebarButtons = new HashMap<>();
    private Map<String, Refreshable> refreshablePanels = new HashMap<>();

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

    @Autowired
    public MainFrame(ClienteController clienteController, MecanicoController mecanicoController, 
                     VeiculoController veiculoController, CatalogoController catalogoController,
                     OrdemServicoController osController, DashboardController dashboardController,
                     ConfigOficinaController configController, EstoqueController estoqueController,
                     FinanceiroController financeiroController, AgendamentoController agendamentoController,
                     NotificacaoController notificacaoController) {
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

        setTitle("Hermes Motor Sport");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 850);
        setLocationRelativeTo(null);
        
        setLayout(new BorderLayout());
        
        initSidebar();
        
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(UIUtils.COLOR_BACKGROUND);
        
        initComponents();
        
        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
        
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
        clienteListPanel = new ClienteListPanel(clienteController, this);
        mecanicoListPanel = new MecanicoListPanel(mecanicoController, this);
        veiculoListPanel = new VeiculoListPanel(veiculoController, this);
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
        
        contentPanel.add(dashboardPanel, "dashboard");
        contentPanel.add(clienteListPanel, "clienteList");
        contentPanel.add(mecanicoListPanel, "mecanicoList");
        contentPanel.add(veiculoListPanel, "veiculoList");
        contentPanel.add(catalogoPanel, "catalogo");
        contentPanel.add(osListPanel, "osList");
        contentPanel.add(osFormPanel, "osForm");
        contentPanel.add(osDetalhePanel, "osDetalhe");
        contentPanel.add(veiculoHistoricoPanel, "veiculoHistorico");
        contentPanel.add(configOficinaPanel, "configOficina");
        contentPanel.add(estoquePanel, "estoque");
        contentPanel.add(financeiroPanel, "financeiro");
        contentPanel.add(agendaPanel, "agenda");
        contentPanel.add(lembretesPanel, "lembretes");

        refreshablePanels.put("dashboard", dashboardPanel);
        refreshablePanels.put("clienteList", clienteListPanel);
        refreshablePanels.put("mecanicoList", mecanicoListPanel);
        refreshablePanels.put("veiculoList", veiculoListPanel);
        refreshablePanels.put("catalogo", catalogoPanel);
        refreshablePanels.put("osList", osListPanel);
        refreshablePanels.put("osForm", osFormPanel);
        refreshablePanels.put("veiculoHistorico", veiculoHistoricoPanel);
        refreshablePanels.put("configOficina", configOficinaPanel);
        refreshablePanels.put("estoque", estoquePanel);
        refreshablePanels.put("financeiro", financeiroPanel);
        refreshablePanels.put("agenda", agendaPanel);
        refreshablePanels.put("lembretes", lembretesPanel);
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
        if (panel != null) panel.refresh();
        cardLayout.show(contentPanel, name);
    }
}
