package projeto.edu.unichristus.java.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.RowFilter;
import javax.swing.SwingWorker;
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
    private final JButton refreshButton;
    private final JButton newButton;
    private final JButton removeButton;
    private final JLabel countLabel;
    private final JLabel modeLabel;
    private List<T> rows = new ArrayList<T>();
    private T editingValue;
    private SwingWorker<List<T>, Void> refreshWorker;
    private int refreshGeneration;
    private boolean loading;
    private boolean operationRunning;
    private String messageAfterRefresh;
    private int messageAfterRefreshType;
    private Integer selectIdAfterRefresh;

    DataModulePanel(String context, String title, String description, String[] columns) {
        super(new BorderLayout(0, 16));
        setBackground(AppTheme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        refreshButton = AppTheme.secondaryButton("Atualizar");
        refreshButton.addActionListener(e -> refreshData());
        add(Ui.moduleHeader(context, title, description, refreshButton), BorderLayout.NORTH);

        JPanel main = new JPanel(new BorderLayout(16, 0));
        main.setOpaque(false);

        JPanel left = Ui.block("Registros", "Busque, selecione e acompanhe os dados cadastrados.");
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
        AppTheme.input(filterField);
        filterField.setColumns(26);
        filterField.setToolTipText("Filtrar registros visiveis");
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
        JPanel filterRow = new JPanel(new BorderLayout(12, 0));
        filterRow.setOpaque(false);
        JPanel filterPanel = Ui.inlineField("Buscar", filterField);
        countLabel = new JLabel("0 registros");
        countLabel.setFont(AppTheme.SMALL);
        countLabel.setForeground(AppTheme.MUTED);
        filterRow.add(filterPanel, BorderLayout.CENTER);
        filterRow.add(countLabel, BorderLayout.EAST);
        tableArea.add(filterRow, BorderLayout.NORTH);
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createLineBorder(AppTheme.LINE));
        tableScroll.getViewport().setBackground(AppTheme.SURFACE);
        tableArea.add(tableScroll, BorderLayout.CENTER);
        left.add(tableArea, BorderLayout.CENTER);

        JPanel right = new JPanel(new GridLayout(2, 1, 0, 16));
        right.setOpaque(false);
        right.setPreferredSize(new Dimension(390, 1));

        JPanel detailBlock = Ui.block("Ficha do registro", "Contexto completo da linha selecionada.");
        details = Ui.detailsArea();
        JScrollPane detailScroll = new JScrollPane(details);
        detailScroll.setBorder(BorderFactory.createLineBorder(AppTheme.LINE));
        detailBlock.add(detailScroll, BorderLayout.CENTER);

        formHost = Ui.block("Cadastro e edicao", "Preencha os campos obrigatorios e salve.");
        formHost.add(buildForm(), BorderLayout.CENTER);

        JPanel actions = new JPanel();
        actions.setOpaque(false);
        actions.setLayout(new BoxLayout(actions, BoxLayout.X_AXIS));
        modeLabel = new JLabel("Modo: novo registro");
        modeLabel.setFont(AppTheme.SMALL);
        modeLabel.setOpaque(true);
        modeLabel.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        newButton = AppTheme.secondaryButton("Novo registro");
        newButton.addActionListener(e -> startNew());
        saveButton = AppTheme.primaryButton("Salvar registro");
        saveButton.addActionListener(e -> saveCurrent());
        removeButton = AppTheme.dangerButton("Remover");
        removeButton.addActionListener(e -> removeSelected());
        actions.add(newButton);
        actions.add(Box.createHorizontalStrut(8));
        actions.add(saveButton);
        actions.add(Box.createHorizontalStrut(8));
        actions.add(removeButton);
        actions.add(Box.createHorizontalGlue());
        actions.add(modeLabel);
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

    protected void onEditingStateChanged(boolean editing) {
    }

    final void refreshData() {
        refreshGeneration++;
        final int generation = refreshGeneration;
        if (refreshWorker != null && !refreshWorker.isDone()) {
            refreshWorker.cancel(true);
        }

        showLoading();
        refreshWorker = new SwingWorker<List<T>, Void>() {
            protected List<T> doInBackground() {
                return loadRows();
            }

            protected void done() {
                if (isCancelled() || generation != refreshGeneration) {
                    return;
                }
                try {
                    applyLoadedRows(get());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    showLoadError("Atualizacao interrompida.");
                } catch (ExecutionException e) {
                    String message = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
                    showLoadError(message);
                }
            }
        };
        refreshWorker.execute();
    }

    private void applyLoadedRows(List<T> loaded) {
        try {
            if (loaded == null) {
                loaded = new ArrayList<T>();
            }
            rows = loaded;
            model.setRowCount(0);
            for (T row : rows) {
                model.addRow(toColumns(row));
            }
            applyFilter();
            setLoading(false);
            if (rows.isEmpty()) {
                details.setText("Nenhum registro encontrado.");
                startNew();
                showRefreshMessageOrDefault("Nenhum registro encontrado. Confira a conexao com o banco ou cadastre um novo item.", 0);
            } else if (table.getRowCount() == 0) {
                details.setText("Nenhum registro corresponde ao filtro atual.");
                showRefreshMessageOrDefault(rows.size() + " registro(s) carregado(s), sem resultado para a busca.", 0);
            } else {
                selectPreferredRow();
                showRefreshMessageOrDefault(rows.size() + " registro(s) carregado(s).", 1);
            }
        } catch (Exception e) {
            showLoadError(e.getMessage());
        } finally {
            clearRefreshFollowUp();
        }
    }

    private void showRefreshMessageOrDefault(String text, int type) {
        showMessage(messageAfterRefresh != null ? messageAfterRefresh : text, messageAfterRefresh != null ? messageAfterRefreshType : type);
    }

    private void selectPreferredRow() {
        int selectedModelRow = -1;
        if (selectIdAfterRefresh != null) {
            for (int i = 0; i < rows.size(); i++) {
                if (idOf(rows.get(i)) == selectIdAfterRefresh.intValue()) {
                    selectedModelRow = i;
                    break;
                }
            }
        }

        int viewRow = selectedModelRow >= 0 ? table.convertRowIndexToView(selectedModelRow) : -1;
        if (viewRow < 0 && table.getRowCount() > 0) {
            viewRow = 0;
        }
        if (viewRow >= 0) {
            table.setRowSelectionInterval(viewRow, viewRow);
        }
    }

    private void clearRefreshFollowUp() {
        messageAfterRefresh = null;
        messageAfterRefreshType = 0;
        selectIdAfterRefresh = null;
    }

    private void showLoading() {
        setLoading(true);
        model.setRowCount(0);
        updateCountLabel();
        details.setText("Carregando dados...");
        showMessage("Carregando " + entityName() + "...", 0);
    }

    private void showLoadError(String message) {
        setLoading(false);
        rows = new ArrayList<T>();
        model.setRowCount(0);
        updateCountLabel();
        details.setText("Nao foi possivel carregar os dados.");
        String detail = message == null || message.trim().isEmpty() ? "erro nao informado" : message;
        showMessage("Erro ao carregar " + entityName() + ": " + detail, 2);
        clearRefreshFollowUp();
    }

    private void setLoading(boolean value) {
        loading = value;
        setControlsEnabled(!value && !operationRunning);
    }

    private void setControlsEnabled(boolean enabled) {
        refreshButton.setEnabled(enabled);
        newButton.setEnabled(enabled);
        saveButton.setEnabled(enabled);
        table.setEnabled(enabled);
        filterField.setEnabled(enabled);
        if (enabled) {
            updateSaveButton();
            updateRemoveButton();
        } else {
            removeButton.setEnabled(false);
        }
    }

    private void updateDetails() {
        if (isBusy()) {
            return;
        }
        T selected = selectedValue();
        details.setText(selected == null ? "Selecione um registro para ver os detalhes." : details(selected));
        details.setCaretPosition(0);
        if (selected == null) {
            editingValue = null;
            clearForm();
            updateSaveButton();
            updateRemoveButton();
            onEditingStateChanged(false);
            return;
        }
        editingValue = selected;
        populateForm(selected);
        updateSaveButton();
        updateRemoveButton();
        onEditingStateChanged(true);
    }

    private void saveCurrent() {
        if (isBusy()) {
            showMessage("Aguarde a operacao atual terminar antes de salvar.", 0);
            return;
        }
        try {
            final boolean wasEditing = editingValue != null;
            T value = readForm();
            if (wasEditing) {
                setId(value, idOf(editingValue));
            }
            final T pending = value;
            runMutation("Salvando " + entityName() + "...", new MutationTask() {
                public boolean execute() {
                    return wasEditing ? update(pending) : save(pending);
                }
            }, new MutationResult() {
                public void handle(boolean changed) {
                    if (!changed) {
                        showMessage(wasEditing ? "Nenhum registro foi atualizado." : "Nenhum registro foi salvo.", 0);
                        return;
                    }
                    afterSave();
                    messageAfterRefresh = wasEditing ? entityName() + " atualizado com sucesso." : entityName() + " salvo com sucesso.";
                    messageAfterRefreshType = 1;
                    selectIdAfterRefresh = Integer.valueOf(idOf(pending));
                    refreshData();
                }
            });
        } catch (IllegalArgumentException e) {
            showMessage(e.getMessage(), 2);
        } catch (Exception e) {
            showMessage("Erro ao salvar " + entityName() + ": " + e.getMessage(), 2);
        }
    }

    private void removeSelected() {
        if (isBusy()) {
            showMessage("Aguarde a operacao atual terminar antes de remover.", 0);
            return;
        }
        final T selected = selectedValue();
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
        runMutation("Removendo " + entityName() + "...", new MutationTask() {
            public boolean execute() {
                return remove(selected);
            }
        }, new MutationResult() {
            public void handle(boolean removed) {
                if (removed) {
                    startNew();
                }
                messageAfterRefresh = removed ? "Registro removido com sucesso." : "Nenhum registro foi removido. Ele pode nao existir mais ou possuir vinculos protegidos.";
                messageAfterRefreshType = removed ? 1 : 0;
                refreshData();
            }
        });
    }

    private void startNew() {
        if (isBusy()) {
            showMessage("Aguarde a operacao atual terminar antes de criar um novo registro.", 0);
            return;
        }
        editingValue = null;
        clearForm();
        table.clearSelection();
        details.setText("Preencha o formulario para criar um novo registro.");
        updateSaveButton();
        updateRemoveButton();
        onEditingStateChanged(false);
    }

    private void runMutation(String message, final MutationTask task, final MutationResult result) {
        setOperationRunning(true);
        showMessage(message, 0);
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            protected Boolean doInBackground() {
                return Boolean.valueOf(task.execute());
            }

            protected void done() {
                setOperationRunning(false);
                try {
                    result.handle(get().booleanValue());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    showMessage("Operacao interrompida.", 2);
                } catch (ExecutionException e) {
                    String message = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
                    showMessage("Erro ao executar operacao em " + entityName() + ": " + safeDetail(message), 2);
                }
            }
        };
        worker.execute();
    }

    private void setOperationRunning(boolean value) {
        operationRunning = value;
        setControlsEnabled(!value && !loading);
    }

    private boolean isBusy() {
        return loading || operationRunning;
    }

    private String safeDetail(String message) {
        return message == null || message.trim().isEmpty() ? "erro nao informado" : message;
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

    private interface MutationTask {
        boolean execute();
    }

    private interface MutationResult {
        void handle(boolean changed);
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
        saveButton.setText(editingValue == null ? "Salvar registro" : "Salvar alteracoes");
        if (modeLabel != null) {
            boolean editing = editingValue != null;
            modeLabel.setText(editing ? "Editando #" + idOf(editingValue) : "Novo registro");
            modeLabel.setForeground(editing ? AppTheme.PRIMARY_DARK : AppTheme.MUTED);
            modeLabel.setBackground(editing ? AppTheme.SUCCESS_BG : AppTheme.SURFACE_ALT);
        }
    }

    private void updateRemoveButton() {
        removeButton.setEnabled(!isBusy() && selectedValue() != null);
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
        updateFilterState();
    }

    private void updateFilterState() {
        updateCountLabel();
        if (isBusy() || rows.isEmpty()) {
            return;
        }
        if (table.getRowCount() == 0) {
            table.clearSelection();
            details.setText("Nenhum registro corresponde ao filtro atual.");
            updateRemoveButton();
            showMessage(rows.size() + " registro(s) carregado(s), sem resultado para a busca.", 0);
            return;
        }
        if (table.getSelectedRow() < 0) {
            table.setRowSelectionInterval(0, 0);
        }
        int visible = table.getRowCount();
        if (visible == rows.size()) {
            showMessage(rows.size() + " registro(s) carregado(s).", 1);
        } else {
            showMessage(visible + " de " + rows.size() + " registro(s) visiveis pelo filtro.", 0);
        }
    }

    private void updateCountLabel() {
        if (countLabel == null || table == null || rows == null) {
            return;
        }
        int total = rows.size();
        int visible = table.getRowCount();
        if (total == 0) {
            countLabel.setText("0 registros");
        } else if (visible == total) {
            countLabel.setText(total + " registro(s)");
        } else {
            countLabel.setText(visible + " de " + total + " visiveis");
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

    protected int parsePositiveInt(JTextField field, String name) {
        int value = parseInt(field, name);
        if (value <= 0) {
            throw new IllegalArgumentException(name + " deve ser maior que zero.");
        }
        return value;
    }

    protected RefOption selectedOption(JComboBox<RefOption> combo, String name) {
        Object selected = combo.getSelectedItem();
        if (!(selected instanceof RefOption) || ((RefOption) selected).id <= 0) {
            throw new IllegalArgumentException(name + " e obrigatorio.");
        }
        return (RefOption) selected;
    }

    protected void selectOption(JComboBox<RefOption> combo, int id) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            RefOption item = combo.getItemAt(i);
            if (item != null && item.id == id) {
                combo.setSelectedIndex(i);
                return;
            }
        }
        combo.setSelectedIndex(0);
    }
}
