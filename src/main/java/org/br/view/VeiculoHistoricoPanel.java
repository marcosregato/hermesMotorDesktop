package org.br.view;

import org.br.controller.VeiculoController;
import org.br.dto.OrdensServicoDTO;
import org.br.utils.UIUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VeiculoHistoricoPanel extends JPanel implements Refreshable {

    private final VeiculoController veiculoController;
    private final MainFrame mainFrame;

    private JTextField txtPlacaBusca;
    private JTable tblHistorico;
    private DefaultTableModel modelHistorico;
    private JLabel lblMotoInfo;

    public VeiculoHistoricoPanel(VeiculoController veiculoController, MainFrame mainFrame) {
        this.veiculoController = veiculoController;
        this.mainFrame = mainFrame;

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        initComponents();
    }

    private void initComponents() {
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        JLabel lblTitulo = new JLabel("Histórico de Manutenções por Placa");
        lblTitulo.setFont(new Font("sans-serif", Font.BOLD, 22));
        topPanel.add(lblTitulo, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtPlacaBusca = new JTextField(15);
        txtPlacaBusca.putClientProperty("JTextField.placeholderText", "Digite a placa (ex: ABC1234)");
        txtPlacaBusca.setPreferredSize(new Dimension(200, 35));
        
        JButton btnBuscar = new JButton("Buscar Histórico");
        UIUtils.setRoundButton(btnBuscar, UIUtils.COLOR_PRIMARY);
        btnBuscar.addActionListener(e -> buscar());

        searchPanel.add(txtPlacaBusca);
        searchPanel.add(btnBuscar);
        topPanel.add(searchPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        lblMotoInfo = new JLabel("Moto não selecionada");
        lblMotoInfo.setForeground(Color.GRAY);
        centerPanel.add(lblMotoInfo, BorderLayout.NORTH);

        modelHistorico = new DefaultTableModel(new String[]{"O.S. #", "Data", "Status", "Total", "Relato do Cliente"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tblHistorico = new JTable(modelHistorico);
        UIUtils.formatTable(tblHistorico);
        centerPanel.add(new JScrollPane(tblHistorico), BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void buscar() {
        String placa = txtPlacaBusca.getText().trim().toUpperCase();
        if (placa.isEmpty()) return;
        try {
            List<OrdensServicoDTO> historico = veiculoController.buscarHistorico(placa);
            modelHistorico.setRowCount(0);
            if (historico.isEmpty()) {
                lblMotoInfo.setText("Nenhum histórico encontrado para a placa: " + placa);
            } else {
                lblMotoInfo.setText("Exibindo histórico para a placa: " + placa);
                for (OrdensServicoDTO os : historico) {
                    modelHistorico.addRow(new Object[]{os.getId(), os.getDataAbertura(), os.getStatus(), "R$ " + os.getValorGeralTotal(), os.getRelatoCliente()});
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Veículo não encontrado.");
            lblMotoInfo.setText("Moto não encontrada.");
            modelHistorico.setRowCount(0);
        }
    }

    @Override
    public void refresh() {
        // Limpar busca anterior ao voltar para a tela
        txtPlacaBusca.setText("");
        modelHistorico.setRowCount(0);
        lblMotoInfo.setText("Moto não selecionada");
    }
}
