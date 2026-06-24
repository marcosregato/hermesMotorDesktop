package org.br.view;

import org.br.controller.AgendamentoController;
import org.br.controller.ClienteController;
import org.br.dto.ClienteDTO;
import org.br.model.Agendamento;
import org.br.model.Cliente;
import org.br.utils.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class AgendaPanel extends JPanel implements Refreshable {

    private final AgendamentoController agendamentoController;
    private final ClienteController clienteController;
    private final MainFrame mainFrame;
    private JList<Agendamento> listAgendamentos;
    private DefaultListModel<Agendamento> modelAgendamentos;
    private JSpinner dateSpinner;

    public AgendaPanel(AgendamentoController agendamentoController, ClienteController clienteController, MainFrame mainFrame) {
        this.agendamentoController = agendamentoController;
        this.clienteController = clienteController;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        setBackground(UIUtils.COLOR_BACKGROUND);
        initComponents();
    }

    private void initComponents() {
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel lblTitulo = new JLabel("Agenda de Atendimentos");
        lblTitulo.setFont(new Font("sans-serif", Font.BOLD, 24));
        headerPanel.add(lblTitulo, BorderLayout.WEST);

        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controlsPanel.setOpaque(false);
        dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
        dateSpinner.setEditor(dateEditor);
        dateSpinner.addChangeListener(e -> refresh());
        
        JButton btnNovoAgendamento = new JButton("Novo Agendamento");
        UIUtils.setRoundButton(btnNovoAgendamento, UIUtils.COLOR_PRIMARY);
        btnNovoAgendamento.addActionListener(e -> novoAgendamento());

        controlsPanel.add(new JLabel("Data:"));
        controlsPanel.add(dateSpinner);
        controlsPanel.add(btnNovoAgendamento);
        headerPanel.add(controlsPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Content
        JPanel card = UIUtils.createCardPanel();
        modelAgendamentos = new DefaultListModel<>();
        listAgendamentos = new JList<>(modelAgendamentos);
        listAgendamentos.setCellRenderer(new AgendamentoRenderer());
        card.add(new JScrollPane(listAgendamentos), BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);
    }

    private void novoAgendamento() {
        JComboBox<ClienteDTO> cbClientes = new JComboBox<>();
        clienteController.listarTodos().forEach(cbClientes::addItem);
        
        JSpinner timeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);

        JTextField txtObs = new JTextField();

        Object[] msg = {"Cliente:", cbClientes, "Horário:", timeSpinner, "Observação:", txtObs};
        int option = JOptionPane.showConfirmDialog(this, msg, "Novo Agendamento", JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            ClienteDTO clienteSelecionado = (ClienteDTO) cbClientes.getSelectedItem();
            if (clienteSelecionado == null) {
                JOptionPane.showMessageDialog(this, "Nenhum cliente selecionado.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDate dataSelecionada = ((Date) dateSpinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalTime horaSelecionada = ((Date) timeSpinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
            LocalDateTime dataHoraAgendamento = dataSelecionada.atTime(horaSelecionada);

            Agendamento agendamento = Agendamento.builder()
                    .cliente(Cliente.builder().id(clienteSelecionado.getId()).build())
                    .dataHora(dataHoraAgendamento)
                    .observacao(txtObs.getText())
                    .build();
            
            agendamentoController.salvar(agendamento);
            refresh(); // Atualiza a lista
        }
    }

    @Override
    public void refresh() {
        modelAgendamentos.clear();
        Date selectedDateValue = (Date) dateSpinner.getValue();
        LocalDate selectedDate = selectedDateValue.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        List<Agendamento> agendamentos = agendamentoController.buscarPorPeriodo(selectedDate.atStartOfDay(), selectedDate.atTime(LocalTime.MAX));
        agendamentos.forEach(modelAgendamentos::addElement);
    }

    static class AgendamentoRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Agendamento a) {
                label.setText(String.format("<html><b>%s</b> - %s<br>%s</html>", 
                    a.getDataHora().format(DateTimeFormatter.ofPattern("HH:mm")),
                    a.getCliente().getNome(),
                    a.getObservacao()));
                label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            }
            return label;
        }
    }
}
