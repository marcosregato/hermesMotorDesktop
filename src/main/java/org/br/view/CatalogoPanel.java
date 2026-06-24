package org.br.view;

import org.br.controller.CatalogoController;
import org.br.dto.PecasCatalogoDTO;
import org.br.dto.ServicosCatalogoDTO;
import org.br.utils.UIUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;

public class CatalogoPanel extends JPanel implements Refreshable {

    private final CatalogoController catalogoController;
    private JTable tblServicos;
    private JTable tblPecas;
    private DefaultTableModel modelServicos;
    private DefaultTableModel modelPecas;

    public CatalogoPanel(CatalogoController catalogoController) {
        this.catalogoController = catalogoController;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        initComponents();
    }

    private void initComponents() {
        JTabbedPane tabbedPane = new JTabbedPane();

        // Painel de Serviços
        JPanel pnlServicos = new JPanel(new BorderLayout(10, 10));
        modelServicos = new DefaultTableModel(new String[]{"ID", "Descrição", "Preço Base"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tblServicos = new JTable(modelServicos);
        UIUtils.formatTable(tblServicos);
        tblServicos.getColumnModel().getColumn(2).setCellRenderer(UIUtils.getCurrencyRenderer());
        
        JButton btnAddServico = new JButton("Adicionar Serviço");
        UIUtils.setRoundButton(btnAddServico, UIUtils.COLOR_PRIMARY);
        btnAddServico.addActionListener(e -> abrirFormServico());
        
        pnlServicos.add(new JScrollPane(tblServicos), BorderLayout.CENTER);
        pnlServicos.add(btnAddServico, BorderLayout.SOUTH);

        // Painel de Peças
        JPanel pnlPecas = new JPanel(new BorderLayout(10, 10));
        modelPecas = new DefaultTableModel(new String[]{"ID", "Código", "Nome", "Preço", "Estoque"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tblPecas = new JTable(modelPecas);
        UIUtils.formatTable(tblPecas);
        tblPecas.getColumnModel().getColumn(3).setCellRenderer(UIUtils.getCurrencyRenderer());
        
        JButton btnAddPeca = new JButton("Adicionar Peça");
        UIUtils.setRoundButton(btnAddPeca, UIUtils.COLOR_PRIMARY);
        btnAddPeca.addActionListener(e -> abrirFormPeca());
        
        pnlPecas.add(new JScrollPane(tblPecas), BorderLayout.CENTER);
        pnlPecas.add(btnAddPeca, BorderLayout.SOUTH);

        tabbedPane.addTab("Serviços", pnlServicos);
        tabbedPane.addTab("Peças", pnlPecas);

        add(tabbedPane, BorderLayout.CENTER);
    }

    @Override
    public void refresh() {
        modelServicos.setRowCount(0);
        catalogoController.listarServicos().forEach(s -> 
            modelServicos.addRow(new Object[]{s.getId(), s.getDescricao(), s.getPrecoBase()}));

        modelPecas.setRowCount(0);
        catalogoController.listarPecas().forEach(p -> 
            modelPecas.addRow(new Object[]{p.getId(), p.getCodigoPeca(), p.getNome(), p.getPrecoVenda(), p.getQuantidadeEstoque()}));
    }

    private void abrirFormServico() {
        JTextField desc = new JTextField();
        JTextField preco = new JTextField();
        Object[] message = {"Descrição:", desc, "Preço Base:", preco};

        int option = JOptionPane.showConfirmDialog(this, message, "Novo Serviço", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                catalogoController.salvarServico(ServicosCatalogoDTO.builder()
                    .descricao(desc.getText())
                    .precoBase(new BigDecimal(preco.getText().replace(",", ".")))
                    .build());
                refresh();
            } catch (Exception e) { JOptionPane.showMessageDialog(this, "Preço inválido!"); }
        }
    }

    private void abrirFormPeca() {
        JTextField cod = new JTextField();
        JTextField nome = new JTextField();
        JTextField preco = new JTextField();
        JTextField qtd = new JTextField();
        Object[] message = {"Código:", cod, "Nome:", nome, "Preço Venda:", preco, "Qtd Estoque:", qtd};

        int option = JOptionPane.showConfirmDialog(this, message, "Nova Peça", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                catalogoController.salvarPeca(PecasCatalogoDTO.builder()
                    .codigoPeca(cod.getText())
                    .nome(nome.getText())
                    .precoVenda(new BigDecimal(preco.getText().replace(",", ".")))
                    .quantidadeEstoque(Integer.parseInt(qtd.getText()))
                    .build());
                refresh();
            } catch (Exception e) { JOptionPane.showMessageDialog(this, "Dados inválidos!"); }
        }
    }
}
