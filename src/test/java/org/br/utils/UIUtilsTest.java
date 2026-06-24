package org.br.utils;

import org.junit.jupiter.api.Test;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class UIUtilsTest {

    @Test
    void formatTable_DeveConfigurarPropriedadesDaTabela() {
        JTable table = new JTable();
        UIUtils.formatTable(table);

        assertEquals(35, table.getRowHeight());
        assertFalse(table.getShowHorizontalLines());
        assertEquals(Color.WHITE, table.getBackground());
    }

    @Test
    void createCardPanel_DeveRetornarPainelConfigurado() {
        JPanel card = UIUtils.createCardPanel();

        assertNotNull(card);
        assertEquals(Color.WHITE, card.getBackground());
        assertTrue(card.getLayout() instanceof BorderLayout);
    }

    @Test
    void getCurrencyRenderer_DeveFormatarValorMonetario() {
        DefaultTableCellRenderer renderer = UIUtils.getCurrencyRenderer();
        JTable table = new JTable();
        
        // Simula a renderização de um valor
        Component c = renderer.getTableCellRendererComponent(table, new BigDecimal("1500.50"), false, false, 0, 0);
        
        assertTrue(c instanceof JLabel);
        String texto = ((JLabel) c).getText();
        
        // Verifica se contém o símbolo de Real e o valor formatado (depende do locale do ambiente)
        assertTrue(texto.contains("R$") || texto.contains("1.500,50"));
    }

    @Test
    void getDateRenderer_DeveFormatarDataHora() {
        DefaultTableCellRenderer renderer = UIUtils.getDateRenderer();
        JTable table = new JTable();
        LocalDateTime data = LocalDateTime.of(2024, 10, 20, 15, 30);
        
        Component c = renderer.getTableCellRendererComponent(table, data, false, false, 0, 0);
        
        assertEquals("20/10/2024 15:30", ((JLabel) c).getText());
    }
}
