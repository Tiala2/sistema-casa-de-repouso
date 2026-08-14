package projeto.edu.unichristus.java.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

final class Ui {
    static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private Ui() {
    }

    static JPanel moduleHeader(String context, String title, String description, Component action) {
        JPanel panel = new JPanel(new BorderLayout(18, 8));
        panel.setBackground(AppTheme.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));

        JPanel copy = new JPanel();
        copy.setOpaque(false);
        copy.setLayout(new BoxLayout(copy, BoxLayout.Y_AXIS));

        JLabel contextLabel = new JLabel(context);
        contextLabel.setForeground(AppTheme.MUTED);
        contextLabel.setFont(AppTheme.SMALL);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(AppTheme.TEXT);
        titleLabel.setFont(AppTheme.TITLE);

        JLabel descriptionLabel = new JLabel(description);
        descriptionLabel.setForeground(AppTheme.MUTED);
        descriptionLabel.setFont(AppTheme.BODY);

        copy.add(contextLabel);
        copy.add(Box.createVerticalStrut(5));
        copy.add(titleLabel);
        copy.add(Box.createVerticalStrut(6));
        copy.add(descriptionLabel);

        panel.add(copy, BorderLayout.CENTER);
        if (action != null) {
            panel.add(action, BorderLayout.EAST);
        }
        return panel;
    }

    static JPanel block(String title, String description) {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        AppTheme.surface(panel);

        JPanel copy = new JPanel();
        copy.setOpaque(false);
        copy.setLayout(new BoxLayout(copy, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(AppTheme.SECTION_TITLE);
        titleLabel.setForeground(AppTheme.TEXT);
        copy.add(titleLabel);

        if (description != null && !description.trim().isEmpty()) {
            JLabel descriptionLabel = new JLabel(description);
            descriptionLabel.setFont(AppTheme.SMALL);
            descriptionLabel.setForeground(AppTheme.MUTED);
            copy.add(Box.createVerticalStrut(4));
            copy.add(descriptionLabel);
        }

        panel.add(copy, BorderLayout.NORTH);
        return panel;
    }

    static JPanel message(String text, int type) {
        JLabel label = new JLabel(text);
        label.setForeground(AppTheme.TEXT);
        label.setFont(AppTheme.BODY);
        return messagePanel(label, type);
    }

    static JPanel messagePanel(JLabel label, int type) {
        JPanel panel = new JPanel(new BorderLayout());
        ColorChoice choice = colorFor(type);
        panel.setBackground(choice.background);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(choice.line),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        label.setForeground(AppTheme.TEXT);
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    static void addField(JPanel form, int row, String label, JTextField field) {
        AppTheme.input(field);
        addComponent(form, row, label, field);
    }

    static void addComponent(JPanel form, int row, String label, java.awt.Component component) {
        if (component instanceof JComboBox) {
            JComboBox<?> combo = (JComboBox<?>) component;
            combo.setFont(AppTheme.BODY);
            combo.setBackground(AppTheme.SURFACE);
            combo.setForeground(AppTheme.TEXT);
        }
        GridBagConstraints left = new GridBagConstraints();
        left.gridx = 0;
        left.gridy = row;
        left.anchor = GridBagConstraints.WEST;
        left.insets = new Insets(0, 0, 10, 12);
        form.add(AppTheme.label(label), left);

        GridBagConstraints right = new GridBagConstraints();
        right.gridx = 1;
        right.gridy = row;
        right.weightx = 1.0;
        right.fill = GridBagConstraints.HORIZONTAL;
        right.insets = new Insets(0, 0, 10, 0);
        form.add(component, right);
    }

    static JPanel formPanel() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        return form;
    }

    static JPanel inlineField(String label, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout(8, 0));
        panel.setOpaque(false);

        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        labelPanel.setOpaque(false);
        labelPanel.add(AppTheme.label(label));

        panel.add(labelPanel, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    static JTextArea detailsArea() {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(AppTheme.BODY);
        area.setForeground(AppTheme.TEXT);
        area.setBackground(AppTheme.SURFACE);
        area.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        return area;
    }

    static String value(Object value) {
        return value == null ? "" : String.valueOf(value);
    }

    static String formatDate(java.time.LocalDate value) {
        return value == null ? "" : DATE.format(value);
    }

    static String formatDateTime(LocalDateTime value) {
        return value == null ? "" : DATE_TIME.format(value);
    }

    static LocalDate parseDate(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(value.trim(), DATE);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(fieldName + " deve usar o formato yyyy-MM-dd.");
        }
    }

    static LocalDate parseRequiredDate(String value, String fieldName) {
        LocalDate parsed = parseDate(value, fieldName);
        if (parsed == null) {
            throw new IllegalArgumentException(fieldName + " e obrigatorio.");
        }
        return parsed;
    }

    static LocalDateTime parseDateTime(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.parse(value.trim(), DATE_TIME);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(fieldName + " deve usar o formato yyyy-MM-dd HH:mm.");
        }
    }

    static LocalDateTime parseRequiredDateTime(String value, String fieldName) {
        LocalDateTime parsed = parseDateTime(value, fieldName);
        if (parsed == null) {
            throw new IllegalArgumentException(fieldName + " e obrigatorio.");
        }
        return parsed;
    }

    private static ColorChoice colorFor(int type) {
        if (type == 1) {
            return new ColorChoice(AppTheme.SUCCESS_BG, new java.awt.Color(178, 218, 194));
        }
        if (type == 2) {
            return new ColorChoice(AppTheme.ERROR_BG, new java.awt.Color(232, 186, 178));
        }
        return new ColorChoice(AppTheme.WARNING_BG, new java.awt.Color(232, 210, 146));
    }

    private static class ColorChoice {
        final java.awt.Color background;
        final java.awt.Color line;

        ColorChoice(java.awt.Color background, java.awt.Color line) {
            this.background = background;
            this.line = line;
        }
    }
}
