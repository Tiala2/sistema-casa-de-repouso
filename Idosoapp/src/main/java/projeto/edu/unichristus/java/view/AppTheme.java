package projeto.edu.unichristus.java.view;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;

final class AppTheme {
    static final Color BACKGROUND = new Color(246, 247, 244);
    static final Color SURFACE = new Color(255, 255, 255);
    static final Color SURFACE_ALT = new Color(236, 241, 238);
    static final Color TEXT = new Color(33, 38, 36);
    static final Color MUTED = new Color(96, 105, 101);
    static final Color LINE = new Color(214, 221, 216);
    static final Color PRIMARY = new Color(42, 91, 76);
    static final Color PRIMARY_DARK = new Color(28, 62, 52);
    static final Color DANGER = new Color(151, 55, 45);
    static final Color WARNING_BG = new Color(255, 248, 224);
    static final Color ERROR_BG = new Color(255, 239, 236);
    static final Color SUCCESS_BG = new Color(232, 246, 238);

    static final Font TITLE = new Font("SansSerif", Font.BOLD, 24);
    static final Font SECTION_TITLE = new Font("SansSerif", Font.BOLD, 18);
    static final Font LABEL = new Font("SansSerif", Font.BOLD, 12);
    static final Font BODY = new Font("SansSerif", Font.PLAIN, 13);
    static final Font SMALL = new Font("SansSerif", Font.PLAIN, 12);

    private AppTheme() {
    }

    static Border panelBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LINE),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        );
    }

    static void surface(JComponent component) {
        component.setBackground(SURFACE);
        component.setBorder(panelBorder());
    }

    static JLabel label(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT);
        label.setFont(LABEL);
        return label;
    }

    static JButton primaryButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(PRIMARY);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(9, 14, 9, 14));
        return button;
    }

    static JButton secondaryButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(SURFACE_ALT);
        button.setForeground(TEXT);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(9, 14, 9, 14));
        return button;
    }

    static JButton dangerButton(String text) {
        JButton button = secondaryButton(text);
        button.setForeground(DANGER);
        return button;
    }

    static void table(JTable table) {
        table.setRowHeight(30);
        table.setFont(BODY);
        table.getTableHeader().setFont(LABEL);
        table.getTableHeader().setBackground(SURFACE_ALT);
        table.getTableHeader().setForeground(TEXT);
        table.setGridColor(LINE);
        table.setSelectionBackground(new Color(214, 232, 225));
        table.setSelectionForeground(TEXT);
        table.setFillsViewportHeight(true);
    }
}
