package projeto.edu.unichristus.java.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
        refresh.addActionListener(e -> refreshMetrics());

        add(Ui.moduleHeader(
            "Inicio",
            "Sistema de Gestao de Casa de Repouso",
            "Ponto de entrada para cadastros, acompanhamento clinico e relatorios operacionais.",
            refresh
        ), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(1, 2, 16, 0));
        center.setOpaque(false);

        JPanel summary = Ui.block("Resumo real do banco", "Contagens calculadas pelas listagens existentes.");
        JPanel summaryGrid = new JPanel(new GridLayout(0, 2, 12, 12));
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

        JPanel modules = Ui.block("Modulos disponiveis", "Acesso direto aos fluxos implementados no dominio.");
        JPanel links = new JPanel(new GridLayout(0, 1, 0, 10));
        links.setOpaque(false);
        link(links, "Cadastro de idosas", "idosas", frame);
        link(links, "Agenda de consultas", "consultas", frame);
        link(links, "Prontuarios medicos", "prontuarios", frame);
        link(links, "Prescricoes e medicacoes", "prescricoes", frame);
        link(links, "Profissionais de saude", "profissionais", frame);
        link(links, "Vacinas", "vacinas", frame);
        link(links, "Eventos sentinela", "eventos", frame);
        link(links, "Relatorios operacionais", "relatorios", frame);
        modules.add(links, BorderLayout.CENTER);

        center.add(summary);
        center.add(modules);
        add(center, BorderLayout.CENTER);
        status.setFont(AppTheme.BODY);
        add(Ui.messagePanel(status, 0), BorderLayout.SOUTH);
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
            status.setText("Resumo atualizado com os dados retornados pelo banco configurado.");
        } else {
            status.setText(failures + " metrica(s) nao carregaram. Verifique a conexao e as tabelas do banco.");
        }
        refresh.setEnabled(true);
    }

    private void setLoading() {
        refresh.setEnabled(false);
        status.setText("Atualizando resumo do banco...");
        for (JLabel label : metricLabels.values()) {
            label.setText("...");
            label.setForeground(AppTheme.MUTED);
        }
    }

    private void showRefreshFailure() {
        refresh.setEnabled(true);
        status.setText("Nao foi possivel atualizar o resumo agora.");
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
        card.setBackground(AppTheme.SURFACE_ALT);
        card.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

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

    private void link(JPanel parent, String label, String key, MainFrame frame) {
        JButton button = AppTheme.secondaryButton(label);
        button.setHorizontalAlignment(JButton.LEFT);
        button.addActionListener(e -> frame.select(key));
        parent.add(button);
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
