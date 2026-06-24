package org.br;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import org.br.service.AuthService;
import org.br.view.LoginDialog;
import org.br.view.MainFrame;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        FlatMacLightLaf.setup();

        ConfigurableApplicationContext context = new SpringApplicationBuilder(Main.class)
                .headless(false)
                .run(args);

        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = context.getBean(MainFrame.class);
            AuthService authService = context.getBean(AuthService.class);

            // Exibe a tela de login antes de mostrar o sistema
            LoginDialog loginDialog = new LoginDialog(mainFrame, authService);
            loginDialog.setVisible(true);

            if (loginDialog.isLoginSucesso()) {
                mainFrame.setVisible(true);
            } else {
                System.exit(0);
            }
        });
    }
}
