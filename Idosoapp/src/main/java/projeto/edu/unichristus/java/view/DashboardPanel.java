package projeto.edu.unichristus.java.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import projeto.edu.unichristus.java.controller.ConsultaController;
import projeto.edu.unichristus.java.controller.EventoSentinelaController;
import projeto.edu.unichristus.java.controller.IdosaController;
import projeto.edu.unichristus.java.controller.PrescricaoController;
import projeto.edu.unichristus.java.controller.ProfissionalSaudeController;
import projeto.edu.unichristus.java.controller.ProntuarioController;
import projeto.edu.unichristus.java.controller.RelatorioController;
import projeto.edu.unichristus.java.controller.VacinaController;

class DashboardPanel extends JPanel {
    private final IdosaController idosaController = new IdosaController();
    private final ConsultaController consultaController = new ConsultaController();
    private final ProntuarioController prontuarioController = new ProntuarioController();
    private final PrescricaoController prescricaoController = new PrescricaoController();
    private final ProfissionalSaudeController profissionalController = new ProfissionalSaudeController();
    private final VacinaController vacinaController = new VacinaController();
    private final EventoSentinelaController eventoController = new EventoSentinelaController();
    private final RelatorioController relatorioController = new RelatorioController();
    private final Map<String, JLabel> metricLabels = new LinkedHashMap<String, JLabel>();
    private final JLabel status = new JLabel();
    private final JButton refresh;
    private SwingWorker<DashboardSnapshot, Void> refreshWorker;
    private int refreshGeneration;

    DashboardPanel(MainFrame frame) {
        super(new BorderLayout(0, 18));
        setBackground(AppTheme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        refresh = AppTheme.primaryButton("Atualizar");
        refresh.setMnemonic('A');
        refresh.setToolTipText("Atualizar indicadores do painel");
        refresh.addActionListener(e -> refreshMetrics());

        add(Ui.moduleHeader(
            "Inicio",
            "Painel operacional",
            "Visao executiva dos cadastros, prontuarios e rotinas assistenciais.",
            refresh
        ), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(16, 16));
        center.setOpaque(false);

        JPanel summary = Ui.block("Indicadores do sistema", "Contagens atualizadas diretamente pelas listagens do banco.");
        JPanel summaryGrid = new JPanel(new GridLayout(2, 4, 12, 12));
        summaryGrid.setOpaque(false);
        addMetric(summaryGrid, "Idosas");
        addMetric(summaryGrid, "Consultas");
        addMetric(summaryGrid, "Prontuarios");
        addMetric(summaryGrid, "Prescricoes");
        addMetric(summaryGrid, "Profissionais");
        addMetric(summaryGrid, "Vacinas");
        addMetric(summaryGrid, "Eventos");
        addMetric(summaryGrid, "Relatorios");
        summary.add(summaryGrid, BorderLayout.CENTER);

        JPanel lower = new JPanel(new GridLayout(1, 2, 16, 0));
        lower.setOpaque(false);

        JPanel flow = Ui.block("Fluxo recomendado", "Ordem natural para alimentar o prontuario completo.");
        JPanel flowSteps = new JPanel(new GridLayout(0, 1, 0, 8));
        flowSteps.setOpaque(false);
        step(flowSteps, "1", "Cadastre profissionais e idosas", "Base para consultas e prontuarios.");
        step(flowSteps, "2", "Abra o prontuario medico", "Conecta a residente aos registros clinicos.");
        step(flowSteps, "3", "Registre consultas e cuidados", "Prescricoes, vacinas e eventos sentinela.");
        step(flowSteps, "4", "Acompanhe por relatorios", "Consolide registros operacionais.");
        flow.add(flowSteps, BorderLayout.CENTER);

        JPanel modules = Ui.block("Acoes rapidas", "Acesso direto aos fluxos mais usados.");
        JPanel links = new JPanel(new GridLayout(0, 2, 10, 10));
        links.setOpaque(false);
        link(links, "Nova idosa", "idosas", frame);
        link(links, "Profissional", "profissionais", frame);
        link(links, "Prontuario", "prontuarios", frame);
        link(links, "Consulta", "consultas", frame);
        link(links, "Prescricao", "prescricoes", frame);
        link(links, "Vacina", "vacinas", frame);
        link(links, "Evento", "eventos", frame);
        link(links, "Relatorio", "relatorios", frame);
        modules.add(links, BorderLayout.CENTER);

        lower.add(flow);
        lower.add(modules);
        center.add(summary, BorderLayout.NORTH);
        center.add(lower, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);
        status.setFont(AppTheme.LABEL);
        status.setHorizontalAlignment(SwingConstants.LEFT);
        add(statusBar(), BorderLayout.SOUTH);
        refreshMetrics();
    }

    void refreshMetrics() {
        refreshGeneration++;
        final int generation = refreshGeneration;
        if (refreshWorker != null && !refreshWorker.isDone()) {
            refreshWorker.cancel(true);
        }

        setLoading();
        refreshWorker = new SwingWorker<DashboardSnapshot, Void>() {
            protected DashboardSnapshot doInBackground() {
                return loadSnapshot(generation);
            }

            protected void done() {
                if (isCancelled()) {
                    return;
                }
                try {
                    DashboardSnapshot snapshot = get();
                    if (snapshot.generation == refreshGeneration) {
                        applySnapshot(snapshot);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    showRefreshFailure();
                } catch (ExecutionException e) {
                    showRefreshFailure();
                }
            }
        };
        refreshWorker.execute();
    }

    private DashboardSnapshot loadSnapshot(int generation) {
        DashboardSnapshot snapshot = new DashboardSnapshot(generation);
        snapshot.put("Idosas", idosaController.listarIdosas());
        snapshot.put("Consultas", consultaController.listarConsultas());
        snapshot.put("Prontuarios", prontuarioController.listarProntuarios());
        snapshot.put("Prescricoes", prescricaoController.listarPrescricoes());
        snapshot.put("Profissionais", profissionalController.listarProfissionais());
        snapshot.put("Vacinas", vacinaController.listarVacinas());
        snapshot.put("Eventos", eventoController.listarEventos());
        snapshot.put("Relatorios", relatorioController.listarRelatorios());
        return snapshot;
    }

    private void applySnapshot(DashboardSnapshot snapshot) {
        int failures = 0;
        for (Map.Entry<String, List<?>> entry : snapshot.metrics.entrySet()) {
            failures += updateMetric(entry.getKey(), entry.getValue());
        }

        if (failures == 0) {
            status.setText("Banco conectado - atualizado em " + Ui.formatDateTime(LocalDateTime.now()));
        } else {
            status.setText(failures + " metrica(s) nao carregaram - ultima tentativa em " + Ui.formatDateTime(LocalDateTime.now()));
        }
        refresh.setEnabled(true);
    }

    private void setLoading() {
        refresh.setEnabled(false);
        status.setText("Atualizando indicadores operacionais...");
        for (JLabel label : metricLabels.values()) {
            label.setText("...");
            label.setForeground(AppTheme.MUTED);
        }
    }

    private void showRefreshFailure() {
        refresh.setEnabled(true);
        status.setText("Nao foi possivel atualizar o painel agora");
        for (JLabel label : metricLabels.values()) {
            label.setText("Erro");
            label.setForeground(AppTheme.DANGER);
        }
    }

    private int updateMetric(String label, List<?> values) {
        JLabel number = metricLabels.get(label);
        if (number == null) {
            return 0;
        }
        if (values == null) {
            number.setText("Erro");
            number.setForeground(AppTheme.DANGER);
            return 1;
        }
        number.setText(String.valueOf(values.size()));
        number.setForeground(AppTheme.PRIMARY_DARK);
        return 0;
    }

    private void addMetric(JPanel parent, String label) {
        JPanel card = new JPanel(new BorderLayout(0, 4));
        card.setBackground(AppTheme.SURFACE);
        card.setToolTipText("Total de " + label.toLowerCase() + " cadastrados");
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.LINE),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));

        JLabel number = new JLabel("-");
        number.setFont(AppTheme.TITLE);
        number.setForeground(AppTheme.PRIMARY_DARK);
        JLabel name = new JLabel(label);
        name.setFont(AppTheme.SMALL);
        name.setForeground(AppTheme.MUTED);
        card.add(number, BorderLayout.CENTER);
        card.add(name, BorderLayout.SOUTH);
        parent.add(card);
        metricLabels.put(label, number);
    }

    private JPanel statusBar() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setBackground(AppTheme.NAV_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        JLabel scope = new JLabel("Ambiente local MySQL");
        scope.setForeground(new Color(183, 199, 213));
        scope.setFont(AppTheme.SMALL);

        status.setForeground(Color.WHITE);
        panel.add(status, BorderLayout.CENTER);
        panel.add(scope, BorderLayout.EAST);
        return panel;
    }

    private void step(JPanel parent, String number, String title, String description) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setOpaque(false);

        JLabel badge = new JLabel(number);
        badge.setOpaque(true);
        badge.setHorizontalAlignment(JLabel.CENTER);
        badge.setForeground(Color.WHITE);
        badge.setBackground(AppTheme.ACCENT);
        badge.setFont(AppTheme.LABEL);
        badge.setBorder(BorderFactory.createEmptyBorder(7, 10, 7, 10));

        JLabel copy = new JLabel("<html><b>" + title + "</b><br><span style='color:#5C6A79'>" + description + "</span></html>");
        copy.setFont(AppTheme.BODY);
        copy.setForeground(AppTheme.TEXT);

        row.add(badge, BorderLayout.WEST);
        row.add(copy, BorderLayout.CENTER);
        parent.add(row);
    }

    private void link(JPanel parent, String label, String key, MainFrame frame) {
        parent.add(actionCard(label, descriptionFor(key), key, frame));
    }

    private JButton actionCard(String title, String description, String key, MainFrame frame) {
        JButton button = new JButton("<html><b>" + title + "</b><br><span style='font-size:10px;color:#5C6A79'>" + description + "</span></html>");
        button.setBackground(AppTheme.SURFACE);
        button.setForeground(AppTheme.TEXT);
        button.setFocusPainted(false);
        button.setHorizontalAlignment(JButton.LEFT);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.LINE),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        button.setToolTipText("Abrir modulo: " + title);
        button.addActionListener(e -> frame.select(key));
        return button;
    }

    private String descriptionFor(String key) {
        if ("idosas".equals(key)) {
            return "Dados da residente";
        }
        if ("profissionais".equals(key)) {
            return "Equipe assistencial";
        }
        if ("prontuarios".equals(key)) {
            return "Historico central";
        }
        if ("consultas".equals(key)) {
            return "Agenda clinica";
        }
        if ("prescricoes".equals(key)) {
            return "Medicacoes";
        }
        if ("vacinas".equals(key)) {
            return "Prevencao";
        }
        if ("eventos".equals(key)) {
            return "Ocorrencias";
        }
        return "Analise operacional";
    }

    private static class DashboardSnapshot {
        final int generation;
        final Map<String, List<?>> metrics = new LinkedHashMap<String, List<?>>();

        DashboardSnapshot(int generation) {
            this.generation = generation;
        }

        void put(String label, List<?> values) {
            metrics.put(label, values);
        }
    }
}
