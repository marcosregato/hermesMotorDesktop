package org.br.view;

import org.br.controller.NotificacaoController;
import org.br.model.Veiculo;
import org.br.utils.UIUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class LembretesPanel extends JPanel implements Refreshable {

    private final NotificacaoController notificacaoController;
    private JTable table;
    private DefaultTableModel model;

    public LembretesPanel(NotificacaoController notificacaoController) {
        this.notificacaoController = notificacaoController;
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        setBackground(UIUtils.COLOR_BACKGROUND);
        initComponents();
    }

    private void initComponents() {
        JLabel lblTitulo = new JLabel("Lembretes de Revisão (Última visita > 6 meses)");
        lblTitulo.setFont(new Font("sans-serif", Font.BOLD, 24));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel card = UIUtils.createCardPanel();
        model = new DefaultTableModel(new String[]{"Cliente", "Telefone", "Veículo", "Placa"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        UIUtils.formatTable(table);
        card.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnEnviarLembrete = new JButton("Enviar Lembrete via WhatsApp");
        UIUtils.setRoundButton(btnEnviarLembrete, new Color(37, 211, 102));
        btnEnviarLembrete.addActionListener(e -> enviarLembrete());
        
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setOpaque(false);
        footer.add(btnEnviarLembrete);
        card.add(footer, BorderLayout.SOUTH);

        add(card, BorderLayout.CENTER);
    }

    private void enviarLembrete() {
        int row = table.getSelectedRow();
        if (row != -1) {
            String telefone = (String) model.getValueAt(row, 1);
            String msg = "Olá! A equipe Hermes Motor notou que sua última revisão já passou de 6 meses. Que tal agendar um check-up?";
            try {
                String url = "https://wa.me/55" + telefone.replaceAll("[^0-9]", "") + "?text=" + URLEncoder.encode(msg, StandardCharsets.UTF_8);
                Desktop.getDesktop().browse(new URI(url));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao abrir WhatsApp.");
            }
        }
    }

    @Override
    public void refresh() {
        model.setRowCount(0);
        notificacaoController.getVeiculosParaLembrete().forEach(v -> 
            model.addRow(new Object[]{
                v.getCliente().getNome(),
                v.getCliente().getTelefone(),
                v.getMarca() + " " + v.getModelo(),
                v.getPlaca()
            })
        );
    }
}
