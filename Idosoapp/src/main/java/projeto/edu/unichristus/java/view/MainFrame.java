package projeto.edu.unichristus.java.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainFrame extends JFrame {
    private final CardLayout cards = new CardLayout();
    private final JPanel content = new JPanel(cards);
    private final Map<String, JButton> navButtons = new LinkedHashMap<String, JButton>();
    private final DashboardPanel dashboardPanel;

    public MainFrame() {
        super("Sistema de Gestao de Casa de Repouso");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1180, 760));
        setLocationByPlatform(true);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppTheme.BACKGROUND);
        root.add(buildNavigation(), BorderLayout.WEST);

        content.setBackground(AppTheme.BACKGROUND);
        dashboardPanel = new DashboardPanel(this);
        addModule("dashboard", "Inicio", dashboardPanel);
        addModule("idosas", "Idosas", new IdosasPanel());
        addModule("consultas", "Consultas", new ConsultasPanel());
        addModule("prontuarios", "Prontuarios", new ProntuariosPanel());
        addModule("prescricoes", "Prescricoes", new PrescricoesPanel());
        addModule("profissionais", "Profissionais", new ProfissionaisPanel());
        addModule("vacinas", "Vacinas", new VacinasPanel());
        addModule("eventos", "Eventos", new EventosPanel());
        addModule("relatorios", "Relatorios", new RelatoriosPanel());
        root.add(content, BorderLayout.CENTER);

        setContentPane(root);
        select("dashboard");
        pack();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    void select(String key) {
        if ("dashboard".equals(key)) {
            dashboardPanel.refreshMetrics();
        }
        cards.show(content, key);
        for (Map.Entry<String, JButton> entry : navButtons.entrySet()) {
            boolean active = entry.getKey().equals(key);
            JButton button = entry.getValue();
            button.setBackground(active ? AppTheme.NAV_ACTIVE : AppTheme.NAV_BG);
            button.setForeground(active ? Color.WHITE : AppTheme.NAV_TEXT);
            button.setBorder(active ? BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, Color.WHITE),
                BorderFactory.createEmptyBorder(10, 10, 10, 12)
            ) : BorderFactory.createEmptyBorder(10, 14, 10, 12));
        }
    }

    private void addModule(String key, String label, JPanel panel) {
        content.add(panel, key);
    }

    private JPanel buildNavigation() {
        JPanel nav = new JPanel(new BorderLayout(0, 16));
        nav.setPreferredSize(new Dimension(260, 1));
        nav.setBackground(AppTheme.NAV_BG);
        nav.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(12, 25, 38)),
            BorderFactory.createEmptyBorder(22, 18, 22, 18)
        ));

        JLabel brand = new JLabel("<html><span style='font-size:18px'><b>Recanto RSC</b></span><br><span style='color:#B7C7D5'>Gestao assistencial integrada</span></html>");
        brand.setForeground(AppTheme.NAV_TEXT);
        brand.setFont(AppTheme.SECTION_TITLE);
        nav.add(brand, BorderLayout.NORTH);

        JPanel links = new JPanel();
        links.setLayout(new BoxLayout(links, BoxLayout.Y_AXIS));
        links.setOpaque(false);
        navSection(links, "Painel");
        navButton(links, "dashboard", "Inicio");
        navSection(links, "Cadastros");
        navButton(links, "idosas", "Cadastro de idosas");
        navButton(links, "profissionais", "Profissionais");
        navSection(links, "Clinico");
        navButton(links, "consultas", "Agenda de consultas");
        navButton(links, "prontuarios", "Prontuarios medicos");
        navButton(links, "prescricoes", "Prescricoes");
        navButton(links, "vacinas", "Vacinas");
        navButton(links, "eventos", "Eventos sentinela");
        navSection(links, "Gestao");
        navButton(links, "relatorios", "Relatorios");
        nav.add(links, BorderLayout.CENTER);

        JLabel status = new JLabel("<html><span style='color:#B7C7D5'>Operacao local conectada ao MySQL</span><br><span style='color:#7F95A8'>Java 8 + Maven + JDBC</span></html>");
        status.setFont(AppTheme.SMALL);
        nav.add(status, BorderLayout.SOUTH);
        return nav;
    }

    private void navSection(JPanel parent, String label) {
        JLabel section = new JLabel(label.toUpperCase());
        section.setFont(AppTheme.SMALL);
        section.setForeground(new Color(127, 149, 168));
        section.setBorder(BorderFactory.createEmptyBorder(14, 8, 6, 8));
        parent.add(section);
    }

    private void navButton(JPanel parent, String key, String label) {
        JButton button = new JButton(label);
        button.setBackground(AppTheme.NAV_BG);
        button.setForeground(AppTheme.NAV_TEXT);
        button.setFocusPainted(false);
        button.setFont(AppTheme.LABEL);
        button.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 12));
        button.setHorizontalAlignment(JButton.LEFT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        button.setToolTipText(descriptionFor(key));
        button.addActionListener(e -> select(key));
        navButtons.put(key, button);
        parent.add(button);
        parent.add(Box.createVerticalStrut(4));
    }

    private String descriptionFor(String key) {
        if ("dashboard".equals(key)) {
            return "Visao geral dos indicadores e atalhos principais";
        }
        if ("idosas".equals(key)) {
            return "Cadastrar e acompanhar dados das residentes";
        }
        if ("profissionais".equals(key)) {
            return "Gerenciar equipe assistencial e registros profissionais";
        }
        if ("consultas".equals(key)) {
            return "Registrar consultas, motivos e diagnosticos";
        }
        if ("prontuarios".equals(key)) {
            return "Centralizar historico clinico da residente";
        }
        if ("prescricoes".equals(key)) {
            return "Registrar medicacoes, posologia e observacoes";
        }
        if ("vacinas".equals(key)) {
            return "Acompanhar vacinas aplicadas";
        }
        if ("eventos".equals(key)) {
            return "Registrar ocorrencias assistenciais relevantes";
        }
        return "Registrar e consultar relatorios operacionais";
    }
}
