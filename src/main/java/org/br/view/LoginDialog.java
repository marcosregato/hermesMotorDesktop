package org.br.view;

import org.br.service.AuthService;
import org.br.utils.UIUtils;

import javax.swing.*;
import java.awt.*;

public class LoginDialog extends JDialog {

    private final AuthService authService;
    private JTextField txtUser;
    private JPasswordField txtPass;
    private boolean loginSucesso = false;

    public LoginDialog(JFrame parent, AuthService authService) {
        super(parent, "Login - Hermes Motor", true);
        this.authService = authService;

        setSize(400, 300);
        setLocationRelativeTo(parent);
        setUndecorated(true); // Estilo moderno sem bordas de janela
        
        initComponents();
    }

    private void initComponents() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 2));
        content.setBackground(Color.WHITE);

        // Header/Logo area
        JPanel header = new JPanel();
        header.setBackground(UIUtils.COLOR_PRIMARY);
        JLabel lblLogo = new JLabel("HERMES MOTOR SPORT");
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setFont(new Font("sans-serif", Font.BOLD, 18));
        lblLogo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        header.add(lblLogo);
        content.add(header, BorderLayout.NORTH);

        // Form area
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("Usuário:"), gbc);
        txtUser = new JTextField(15);
        txtUser.setPreferredSize(new Dimension(0, 35));
        gbc.gridx = 1; form.add(txtUser, gbc);

        gbc.gridx = 0; gbc.gridy = 1; form.add(new JLabel("Senha:"), gbc);
        txtPass = new JPasswordField(15);
        txtPass.setPreferredSize(new Dimension(0, 35));
        gbc.gridx = 1; form.add(txtPass, gbc);

        content.add(form, BorderLayout.CENTER);

        // Buttons
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttons.setOpaque(false);
        
        JButton btnEntrar = new JButton("Entrar");
        UIUtils.setRoundButton(btnEntrar, UIUtils.COLOR_PRIMARY);
        btnEntrar.addActionListener(e -> tentarLogin());

        JButton btnSair = new JButton("Sair");
        UIUtils.setRoundButton(btnSair, Color.GRAY);
        btnSair.addActionListener(e -> System.exit(0));

        buttons.add(btnSair);
        buttons.add(btnEntrar);
        content.add(buttons, BorderLayout.SOUTH);

        add(content);
        
        // Enter key support
        rootPane.setDefaultButton(btnEntrar);
    }

    private void tentarLogin() {
        String user = txtUser.getText();
        String pass = new String(txtPass.getPassword());

        if (authService.login(user, pass)) {
            loginSucesso = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Usuário ou senha incorretos!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isLoginSucesso() {
        return loginSucesso;
    }
}
