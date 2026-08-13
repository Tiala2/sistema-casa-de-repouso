package projeto.edu.unichristus.java.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import projeto.edu.unichristus.java.controller.ConsultaController;
import projeto.edu.unichristus.java.controller.EventoSentinelaController;
import projeto.edu.unichristus.java.controller.IdosaController;
import projeto.edu.unichristus.java.controller.PrescricaoController;
import projeto.edu.unichristus.java.controller.ProfissionalSaudeController;
import projeto.edu.unichristus.java.controller.ProntuarioController;
import projeto.edu.unichristus.java.controller.RelatorioController;
import projeto.edu.unichristus.java.controller.VacinaController;

class DashboardPanel extends JPanel {
    DashboardPanel(MainFrame frame) {
        super(new BorderLayout(0, 18));
        setBackground(AppTheme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        add(Ui.moduleHeader(
            "Inicio",
            "Sistema de Gestao de Casa de Repouso",
            "Ponto de entrada para cadastros, acompanhamento clinico e relatorios operacionais.",
            null
        ), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(1, 2, 16, 0));
        center.setOpaque(false);

        JPanel summary = Ui.block("Resumo real do banco", "Contagens calculadas pelas listagens existentes.");
        JPanel summaryGrid = new JPanel(new GridLayout(0, 2, 12, 12));
        summaryGrid.setOpaque(false);
        addMetric(summaryGrid, "Idosas", count(new IdosaController().listarIdosas()));
        addMetric(summaryGrid, "Consultas", count(new ConsultaController().listarConsultas()));
        addMetric(summaryGrid, "Prontuarios", count(new ProntuarioController().listarProntuarios()));
        addMetric(summaryGrid, "Prescricoes", count(new PrescricaoController().listarPrescricoes()));
        addMetric(summaryGrid, "Profissionais", count(new ProfissionalSaudeController().listarProfissionais()));
        addMetric(summaryGrid, "Vacinas", count(new VacinaController().listarVacinas()));
        addMetric(summaryGrid, "Eventos", count(new EventoSentinelaController().listarEventos()));
        addMetric(summaryGrid, "Relatorios", count(new RelatorioController().listarRelatorios()));
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
        add(Ui.message("Os valores acima sao exibidos apenas quando retornam do banco configurado.", 0), BorderLayout.SOUTH);
    }

    private String count(List<?> values) {
        return values == null ? "Erro" : String.valueOf(values.size());
    }

    private void addMetric(JPanel parent, String label, String value) {
        JPanel card = new JPanel(new BorderLayout(0, 4));
        card.setBackground(AppTheme.SURFACE_ALT);
        card.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JLabel number = new JLabel(value);
        number.setFont(AppTheme.TITLE);
        number.setForeground(AppTheme.PRIMARY_DARK);
        JLabel name = new JLabel(label);
        name.setFont(AppTheme.SMALL);
        name.setForeground(AppTheme.MUTED);
        card.add(number, BorderLayout.CENTER);
        card.add(name, BorderLayout.SOUTH);
        parent.add(card);
    }

    private void link(JPanel parent, String label, String key, MainFrame frame) {
        JButton button = AppTheme.secondaryButton(label);
        button.setHorizontalAlignment(JButton.LEFT);
        button.addActionListener(e -> frame.select(key));
        parent.add(button);
    }
}
