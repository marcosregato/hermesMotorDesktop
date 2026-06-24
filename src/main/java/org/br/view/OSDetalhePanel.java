package org.br.view;

import org.br.controller.CatalogoController;
import org.br.controller.ConfigOficinaController;
import org.br.controller.OrdemServicoController;
import org.br.dto.OrdensServicoDTO;
import org.br.dto.OsItemDTO;
import org.br.dto.PecasCatalogoDTO;
import org.br.dto.ServicosCatalogoDTO;
import org.br.model.ConfigOficina;
import org.br.model.StatusOS;
import org.br.utils.PdfGenerator;
import org.br.utils.UIUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

public class OSDetalhePanel extends JPanel implements Refreshable {

    private final OrdemServicoController osController;
    private final CatalogoController catalogoController;
    private final ConfigOficinaController configController;
    private final MainFrame mainFrame;
    private OrdensServicoDTO currentOS;

    private JLabel lblOSInfo, lblTotal, lblTempo;
    private JTable tblItens;
    private DefaultTableModel modelItens;
    private JComboBox<StatusOS> cbStatus;
    private JButton btnPlay, btnStop;

    public OSDetalhePanel(OrdemServicoController osController, CatalogoController catalogoController, 
                          ConfigOficinaController configController, MainFrame mainFrame) {
        this.osController = osController;
        this.catalogoController = catalogoController;
        this.configController = configController;
        this.mainFrame = mainFrame;

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        setBackground(UIUtils.COLOR_BACKGROUND);
        initComponents();
    }

    private void initComponents() {
        // Header
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        lblOSInfo = new JLabel("Detalhes da Ordem de Serviço");
        lblOSInfo.setFont(new Font("sans-serif", Font.BOLD, 24));
        
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        statusPanel.setOpaque(false);
        statusPanel.add(new JLabel("Status:"));
        cbStatus = new JComboBox<>(StatusOS.values());
        cbStatus.addActionListener(e -> alterarStatus());
        statusPanel.add(cbStatus);

        topPanel.add(lblOSInfo, BorderLayout.WEST);
        topPanel.add(statusPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Card
        JPanel card = UIUtils.createCardPanel();
        
        modelItens = new DefaultTableModel(new String[]{"ID", "Tipo", "Descrição", "Qtd", "Vlr. Unit", "Subtotal"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tblItens = new JTable(modelItens);
        UIUtils.formatTable(tblItens);
        tblItens.getColumnModel().getColumn(4).setCellRenderer(UIUtils.getCurrencyRenderer());
        tblItens.getColumnModel().getColumn(5).setCellRenderer(UIUtils.getCurrencyRenderer());
        
        JScrollPane scrollPane = new JScrollPane(tblItens);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        card.add(scrollPane, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);
        
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonsPanel.setOpaque(false);
        btnPlay = new JButton("▶️ Iniciar Atendimento");
        btnStop = new JButton("⏹️ Finalizar Atendimento");
        JButton btnAddServico = new JButton("Add Serviço");
        JButton btnAddPeca = new JButton("Add Peça");
        JButton btnImprimir = new JButton("Gerar PDF");
        JButton btnWhatsapp = new JButton("WhatsApp");
        UIUtils.setRoundButton(btnWhatsapp, new Color(37, 211, 102));
        JButton btnVoltar = new JButton("Voltar");

        btnPlay.addActionListener(e -> osController.iniciarAtendimento(currentOS.getId()));
        btnStop.addActionListener(e -> osController.finalizarAtendimento(currentOS.getId()));
        btnWhatsapp.addActionListener(e -> enviarWhatsapp());
        btnAddServico.addActionListener(e -> mostrarSelecaoServico());
        btnAddPeca.addActionListener(e -> mostrarSelecaoPeca());
        btnImprimir.addActionListener(e -> gerarPdf());
        btnVoltar.addActionListener(e -> mainFrame.showPanel("osList"));

        buttonsPanel.add(btnPlay);
        buttonsPanel.add(btnStop);
        buttonsPanel.add(btnAddServico);
        buttonsPanel.add(btnAddPeca);
        buttonsPanel.add(btnWhatsapp);
        buttonsPanel.add(btnImprimir);
        buttonsPanel.add(btnVoltar);

        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        infoPanel.setOpaque(false);
        lblTempo = new JLabel("Tempo: 0h 0m");
        lblTotal = new JLabel("Total: R$ 0,00");
        lblTempo.setFont(new Font("sans-serif", Font.PLAIN, 14));
        lblTotal.setFont(new Font("sans-serif", Font.BOLD, 20));
        infoPanel.add(lblTempo);
        infoPanel.add(lblTotal);
        
        footerPanel.add(buttonsPanel, BorderLayout.WEST);
        footerPanel.add(infoPanel, BorderLayout.EAST);
        
        add(card, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }

    @Override
    public void refresh() {
        if (currentOS != null) {
            currentOS = osController.buscarPorId(currentOS.getId());
            atualizarTabela();
        }
    }

    public void setOS(OrdensServicoDTO os) {
        this.currentOS = os;
        lblOSInfo.setText("O.S. #" + os.getId());
        cbStatus.setSelectedItem(os.getStatus());
        atualizarTabela();
    }

    private void atualizarTabela() {
        if (currentOS == null) return;
        modelItens.setRowCount(0);
        osController.listarItensDaOS(currentOS.getId()).forEach(item -> 
            modelItens.addRow(new Object[]{item.getId(), item.getTipo(), item.getDescricao(), item.getQuantidade(), item.getValorUnitario(), item.getSubtotal()})
        );
        lblTotal.setText("Total: R$ " + currentOS.getValorGeralTotal());
        
        // Atualiza tempo de atendimento
        if (currentOS.getInicioAtendimento() != null && currentOS.getFimAtendimento() != null) {
            Duration dur = Duration.between(currentOS.getInicioAtendimento(), currentOS.getFimAtendimento());
            lblTempo.setText(String.format("Tempo: %dh %dm", dur.toHours(), dur.toMinutesPart()));
        } else {
            lblTempo.setText("Tempo: N/A");
        }
    }
    
    // ... (demais métodos permanecem iguais)
    private void alterarStatus() { if (currentOS != null) osController.alterarStatus(currentOS.getId(), (StatusOS) cbStatus.getSelectedItem()); }
    private void enviarWhatsapp() { try { ConfigOficina config = configController.getConfig(); String msg = config.getMensagemPadraoWhatsapp() + "\nOS #" + currentOS.getId() + "\nTotal: R$ " + currentOS.getValorGeralTotal(); String url = "https://wa.me/?text=" + URLEncoder.encode(msg, StandardCharsets.UTF_8); Desktop.getDesktop().browse(new URI(url)); } catch (Exception e) { JOptionPane.showMessageDialog(this, "Erro ao abrir WhatsApp."); } }
    private void gerarPdf() { if (currentOS == null) return; JFileChooser fc = new JFileChooser(); fc.setSelectedFile(new File("OS_" + currentOS.getId() + ".pdf")); if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) PdfGenerator.gerarRelatorioOS(currentOS, osController.listarItensDaOS(currentOS.getId()), fc.getSelectedFile().getAbsolutePath()); }
    private void mostrarSelecaoServico() { JComboBox<ServicosCatalogoDTO> cb = new JComboBox<>(); catalogoController.listarServicos().forEach(cb::addItem); if (JOptionPane.showConfirmDialog(this, cb, "Selecionar Serviço", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) { osController.adicionarServico(currentOS.getId(), ((ServicosCatalogoDTO)cb.getSelectedItem()).getId(), 1); refresh(); } }
    private void mostrarSelecaoPeca() { JComboBox<PecasCatalogoDTO> cb = new JComboBox<>(); catalogoController.listarPecas().forEach(cb::addItem); if (JOptionPane.showConfirmDialog(this, cb, "Selecionar Peça", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) { try { osController.adicionarPeca(currentOS.getId(), ((PecasCatalogoDTO)cb.getSelectedItem()).getId(), 1); refresh(); } catch (Exception e) { JOptionPane.showMessageDialog(this, e.getMessage()); } } }
}
