package projeto.edu.unichristus.java.view;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;

final class AppTheme {
    static final Color BACKGROUND = new Color(244, 247, 251);
    static final Color SURFACE = new Color(255, 255, 255);
    static final Color SURFACE_ALT = new Color(235, 243, 249);
    static final Color TEXT = new Color(27, 39, 51);
    static final Color MUTED = new Color(92, 106, 121);
    static final Color LINE = new Color(213, 222, 232);
    static final Color PRIMARY = new Color(0, 125, 101);
    static final Color PRIMARY_DARK = new Color(0, 82, 72);
    static final Color ACCENT = new Color(0, 150, 199);
    static final Color NAV_BG = new Color(21, 37, 53);
    static final Color NAV_ACTIVE = new Color(0, 125, 101);
    static final Color NAV_TEXT = new Color(231, 239, 246);
    static final Color DANGER = new Color(177, 58, 46);
    static final Color WARNING_BG = new Color(255, 249, 229);
    static final Color ERROR_BG = new Color(255, 239, 236);
    static final Color SUCCESS_BG = new Color(231, 248, 241);

    static final Font TITLE = new Font("SansSerif", Font.BOLD, 26);
    static final Font SECTION_TITLE = new Font("SansSerif", Font.BOLD, 17);
    static final Font LABEL = new Font("SansSerif", Font.BOLD, 12);
    static final Font BODY = new Font("SansSerif", Font.PLAIN, 13);
    static final Font SMALL = new Font("SansSerif", Font.PLAIN, 12);

    private AppTheme() {
    }

    static Border panelBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LINE),
            BorderFactory.createEmptyBorder(18, 18, 18, 18)
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
        button.setFont(LABEL);
        button.setBorder(BorderFactory.createEmptyBorder(9, 14, 9, 14));
        return button;
    }

    static JButton secondaryButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(SURFACE_ALT);
        button.setForeground(TEXT);
        button.setFocusPainted(false);
        button.setFont(LABEL);
        button.setBorder(BorderFactory.createEmptyBorder(9, 14, 9, 14));
        return button;
    }

    static JButton dangerButton(String text) {
        JButton button = secondaryButton(text);
        button.setForeground(DANGER);
        return button;
    }

    static void input(JTextField field) {
        field.setFont(BODY);
        field.setForeground(TEXT);
        field.setBackground(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LINE),
            BorderFactory.createEmptyBorder(8, 9, 8, 9)
        ));
    }

    static void table(JTable table) {
        table.setRowHeight(34);
        table.setFont(BODY);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new java.awt.Dimension(0, 1));
        table.getTableHeader().setFont(LABEL);
        table.getTableHeader().setBackground(new Color(225, 237, 246));
        table.getTableHeader().setForeground(TEXT);
        table.setGridColor(LINE);
        table.setSelectionBackground(new Color(210, 238, 231));
        table.setSelectionForeground(TEXT);
        table.setFillsViewportHeight(true);
    }
}
