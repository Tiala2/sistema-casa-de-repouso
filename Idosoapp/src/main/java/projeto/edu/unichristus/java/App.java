package projeto.edu.unichristus.java;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import projeto.edu.unichristus.java.view.MainFrame;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ignored) {
                    // Keep Swing default look and feel when the system one is unavailable.
                }
                new MainFrame().setVisible(true);
            }
        });
    }
}
