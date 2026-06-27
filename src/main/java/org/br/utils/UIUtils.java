package org.br.utils;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class UIUtils {

    public static final Color COLOR_PRIMARY = new Color(0, 122, 255);
    public static final Color COLOR_DANGER = new Color(255, 59, 48);
    public static final Color COLOR_SUCCESS = new Color(52, 199, 89);
    public static final Color COLOR_BACKGROUND = new Color(250, 250, 251); // Off-White
    public static final Color COLOR_SIDEBAR = new Color(245, 245, 247);
    
    private static final Locale BRAZIL = new Locale("pt", "BR");
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(BRAZIL);
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static void formatTable(JTable table) {
        table.setRowHeight(35);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.getTableHeader().setFont(new Font("sans-serif", Font.BOLD, 13));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        table.setBackground(Color.WHITE);
        table.setSelectionBackground(new Color(232, 242, 255));
        table.setSelectionForeground(Color.BLACK);
    }

    public static void setRoundButton(JButton btn, Color background) {
        btn.putClientProperty("JButton.buttonType", "roundRect");
        btn.setBackground(background);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("sans-serif", Font.BOLD, 13));
    }

    public static void setErrorState(JComponent component, boolean isError) {
        component.putClientProperty(FlatClientProperties.OUTLINE, isError ? FlatClientProperties.OUTLINE_ERROR : null);
        component.repaint();
    }

    public static JPanel createCardPanel() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.putClientProperty(FlatClientProperties.STYLE, "arc: 20");
        card.setBorder(new EmptyBorder(15, 15, 15, 15));
        return card;
    }

    public static DefaultTableCellRenderer getCurrencyRenderer() {
        return new DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object value) {
                if (value instanceof BigDecimal) {
                    setText(CURRENCY_FORMAT.format(value));
                    setHorizontalAlignment(JLabel.RIGHT);
                } else {
                    super.setValue(value);
                }
            }
        };
    }

    public static DefaultTableCellRenderer getDateRenderer() {
        return new DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object value) {
                if (value instanceof LocalDateTime) {
                    setText(((LocalDateTime) value).format(DATE_FORMAT));
                } else {
                    super.setValue(value);
                }
            }
        };
    }
}
