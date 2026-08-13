package projeto.edu.unichristus.java.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainFrame extends JFrame {
    private final CardLayout cards = new CardLayout();
    private final JPanel content = new JPanel(cards);
    private final Map<String, JButton> navButtons = new LinkedHashMap<String, JButton>();

    public MainFrame() {
        super("Sistema de Gestao de Casa de Repouso");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1180, 760));
        setLocationByPlatform(true);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppTheme.BACKGROUND);
        root.add(buildNavigation(), BorderLayout.WEST);

        content.setBackground(AppTheme.BACKGROUND);
        addModule("dashboard", "Inicio", new DashboardPanel(this));
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
        cards.show(content, key);
        for (Map.Entry<String, JButton> entry : navButtons.entrySet()) {
            boolean active = entry.getKey().equals(key);
            entry.getValue().setBackground(active ? AppTheme.PRIMARY : AppTheme.SURFACE);
            entry.getValue().setForeground(active ? java.awt.Color.WHITE : AppTheme.TEXT);
        }
    }

    private void addModule(String key, String label, JPanel panel) {
        content.add(panel, key);
    }

    private JPanel buildNavigation() {
        JPanel nav = new JPanel(new BorderLayout(0, 16));
        nav.setPreferredSize(new Dimension(235, 1));
        nav.setBackground(AppTheme.SURFACE);
        nav.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 1, AppTheme.LINE),
            BorderFactory.createEmptyBorder(24, 18, 24, 18)
        ));

        JLabel brand = new JLabel("<html><b>Recanto RSC</b><br><span style='color:#606965'>Gestao assistencial</span></html>");
        brand.setForeground(AppTheme.TEXT);
        brand.setFont(AppTheme.SECTION_TITLE);
        nav.add(brand, BorderLayout.NORTH);

        JPanel links = new JPanel(new GridLayout(0, 1, 0, 8));
        links.setOpaque(false);
        navButton(links, "dashboard", "Inicio");
        navButton(links, "idosas", "Cadastro de idosas");
        navButton(links, "consultas", "Agenda de consultas");
        navButton(links, "prontuarios", "Prontuarios medicos");
        navButton(links, "prescricoes", "Prescricoes");
        navButton(links, "profissionais", "Profissionais");
        navButton(links, "vacinas", "Vacinas");
        navButton(links, "eventos", "Eventos sentinela");
        navButton(links, "relatorios", "Relatorios");
        nav.add(links, BorderLayout.CENTER);

        JLabel status = new JLabel("<html><span style='color:#606965'>Java 8 + Maven + JDBC</span></html>");
        status.setFont(AppTheme.SMALL);
        nav.add(status, BorderLayout.SOUTH);
        return nav;
    }

    private void navButton(JPanel parent, String key, String label) {
        JButton button = AppTheme.secondaryButton(label);
        button.setHorizontalAlignment(JButton.LEFT);
        button.addActionListener(e -> select(key));
        navButtons.put(key, button);
        parent.add(button);
    }
}
