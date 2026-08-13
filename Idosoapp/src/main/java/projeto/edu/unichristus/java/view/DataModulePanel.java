package projeto.edu.unichristus.java.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

abstract class DataModulePanel<T> extends JPanel {
    private final DefaultTableModel model;
    private final JTable table;
    private final TableRowSorter<DefaultTableModel> sorter;
    private final JTextField filterField;
    private final JTextArea details;
    private final JPanel messageHost;
    private final JPanel formHost;
    private final JButton saveButton;
    private List<T> rows = new ArrayList<T>();
    private T editingValue;

    DataModulePanel(String context, String title, String description, String[] columns) {
        super(new BorderLayout(0, 16));
        setBackground(AppTheme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        JButton refresh = AppTheme.secondaryButton("Atualizar");
        refresh.addActionListener(e -> refreshData());
        add(Ui.moduleHeader(context, title, description, refresh), BorderLayout.NORTH);

        JPanel main = new JPanel(new BorderLayout(16, 0));
        main.setOpaque(false);

        JPanel left = Ui.block("Conteudo principal", "Registros carregados das consultas existentes.");
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        sorter = new TableRowSorter<DefaultTableModel>(model);
        table.setRowSorter(sorter);
        AppTheme.table(table);
        table.getSelectionModel().addListSelectionListener(e -> updateDetails());

        JPanel tableArea = new JPanel(new BorderLayout(0, 10));
        tableArea.setOpaque(false);
        filterField = new JTextField();
        filterField.setFont(AppTheme.BODY);
        filterField.setToolTipText("Filtrar registros carregados");
        filterField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                applyFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                applyFilter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                applyFilter();
            }
        });
        JPanel filterPanel = Ui.inlineField("Buscar", filterField);
        tableArea.add(filterPanel, BorderLayout.NORTH);
        tableArea.add(new JScrollPane(table), BorderLayout.CENTER);
        left.add(tableArea, BorderLayout.CENTER);

        JPanel right = new JPanel(new GridLayout(2, 1, 0, 16));
        right.setOpaque(false);

        JPanel detailBlock = Ui.block("Detalhes do registro", "Selecione uma linha para revisar o contexto.");
        details = Ui.detailsArea();
        detailBlock.add(new JScrollPane(details), BorderLayout.CENTER);

        formHost = Ui.block("Acao principal", "Preencha os campos obrigatorios e salve.");
        formHost.add(buildForm(), BorderLayout.CENTER);

        JPanel actions = new JPanel();
        actions.setOpaque(false);
        actions.setLayout(new BoxLayout(actions, BoxLayout.X_AXIS));
        JButton newButton = AppTheme.secondaryButton("Novo");
        newButton.addActionListener(e -> startNew());
        saveButton = AppTheme.primaryButton("Salvar");
        saveButton.addActionListener(e -> saveCurrent());
        JButton remove = AppTheme.dangerButton("Remover selecionado");
        remove.addActionListener(e -> removeSelected());
        actions.add(newButton);
        actions.add(Box.createHorizontalStrut(8));
        actions.add(saveButton);
        actions.add(Box.createHorizontalStrut(8));
        actions.add(remove);
        actions.add(Box.createHorizontalGlue());
        formHost.add(actions, BorderLayout.SOUTH);

        right.add(detailBlock);
        right.add(formHost);

        main.add(left, BorderLayout.CENTER);
        main.add(right, BorderLayout.EAST);
        add(main, BorderLayout.CENTER);

        messageHost = new JPanel(new BorderLayout());
        messageHost.setOpaque(false);
        add(messageHost, BorderLayout.SOUTH);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                refreshData();
            }
        });
    }

    protected abstract JPanel buildForm();

    protected abstract List<T> loadRows();

    protected abstract Object[] toColumns(T value);

    protected abstract String details(T value);

    protected abstract T readForm();

    protected abstract boolean save(T value);

    protected abstract boolean update(T value);

    protected abstract boolean remove(T value);

    protected abstract String entityName();

    protected abstract int idOf(T value);

    protected abstract void setId(T value, int id);

    protected abstract void populateForm(T value);

    protected abstract void clearForm();

    protected void afterSave() {
    }

    final void refreshData() {
        try {
            List<T> loaded = loadRows();
            if (loaded == null) {
                throw new IllegalStateException("A consulta retornou erro. Verifique a configuracao do banco de dados.");
            }
            rows = loaded;
            model.setRowCount(0);
            for (T row : rows) {
                model.addRow(toColumns(row));
            }
            applyFilter();
            if (rows.isEmpty()) {
                details.setText("Nenhum registro encontrado.");
                startNew();
                showMessage("Nenhum registro encontrado. Confira a conexao com o banco ou cadastre um novo item.", 0);
            } else if (table.getRowCount() == 0) {
                details.setText("Nenhum registro corresponde ao filtro atual.");
                showMessage(rows.size() + " registro(s) carregado(s), sem resultado para a busca.", 0);
            } else {
                table.setRowSelectionInterval(0, 0);
                showMessage(rows.size() + " registro(s) carregado(s).", 1);
            }
        } catch (Exception e) {
            rows = new ArrayList<T>();
            model.setRowCount(0);
            details.setText("Nao foi possivel carregar os dados.");
            showMessage("Erro ao carregar " + entityName() + ": " + e.getMessage(), 2);
        }
    }

    private void updateDetails() {
        T selected = selectedValue();
        details.setText(selected == null ? "Selecione um registro para ver os detalhes." : details(selected));
        details.setCaretPosition(0);
        if (selected != null) {
            editingValue = selected;
            populateForm(selected);
            updateSaveButton();
        }
    }

    private void saveCurrent() {
        try {
            boolean wasEditing = editingValue != null;
            T value = readForm();
            boolean changed;
            if (!wasEditing) {
                changed = save(value);
            } else {
                setId(value, idOf(editingValue));
                changed = update(value);
            }
            if (!changed) {
                showMessage(wasEditing ? "Nenhum registro foi atualizado." : "Nenhum registro foi salvo.", 0);
                return;
            }
            afterSave();
            refreshData();
            showMessage(wasEditing ? entityName() + " atualizado com sucesso." : entityName() + " salvo com sucesso.", 1);
        } catch (IllegalArgumentException e) {
            showMessage(e.getMessage(), 2);
        } catch (Exception e) {
            showMessage("Erro ao salvar " + entityName() + ": " + e.getMessage(), 2);
        }
    }

    private void removeSelected() {
        T selected = selectedValue();
        if (selected == null) {
            showMessage("Selecione um registro antes de remover.", 0);
            return;
        }
        int answer = JOptionPane.showConfirmDialog(
            this,
            "Remover o registro selecionado?",
            "Confirmar remocao",
            JOptionPane.YES_NO_OPTION
        );
        if (answer != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            boolean removed = remove(selected);
            refreshData();
            if (removed) {
                startNew();
            }
            showMessage(removed ? "Registro removido com sucesso." : "Nenhum registro foi removido.", removed ? 1 : 0);
        } catch (Exception e) {
            showMessage("Erro ao remover " + entityName() + ": " + e.getMessage(), 2);
        }
    }

    private void startNew() {
        editingValue = null;
        clearForm();
        table.clearSelection();
        details.setText("Preencha o formulario para criar um novo registro.");
        updateSaveButton();
    }

    private T selectedValue() {
        int row = table.getSelectedRow();
        if (row < 0) {
            return null;
        }
        int modelRow = table.convertRowIndexToModel(row);
        if (modelRow < 0 || modelRow >= rows.size()) {
            return null;
        }
        return rows.get(modelRow);
    }

    protected void showMessage(String text, int type) {
        messageHost.removeAll();
        messageHost.add(Ui.message(text, type), BorderLayout.CENTER);
        messageHost.revalidate();
        messageHost.repaint();
    }

    protected Component formHost() {
        return formHost;
    }

    protected boolean isEditing() {
        return editingValue != null;
    }

    private void updateSaveButton() {
        saveButton.setText(editingValue == null ? "Salvar" : "Salvar alteracoes");
    }

    private void applyFilter() {
        if (filterField == null) {
            return;
        }
        String text = filterField.getText();
        if (text == null || text.trim().isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(text.trim())));
        }
    }

    protected void require(JTextField field, String name) {
        if (field.getText() == null || field.getText().trim().isEmpty()) {
            throw new IllegalArgumentException(name + " e obrigatorio.");
        }
    }

    protected String text(JTextField field) {
        return field.getText() == null ? "" : field.getText().trim();
    }

    protected String optionalText(JTextField field) {
        String value = text(field);
        return value.isEmpty() ? null : value;
    }

    protected int parseInt(JTextField field, String name) {
        require(field, name);
        try {
            return Integer.parseInt(field.getText().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(name + " deve ser um numero inteiro.");
        }
    }
}
