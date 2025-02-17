import javax.swing.SwingUtilities;
import vista.LaberintoGUI;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LaberintoGUI().setVisible(true));
    }
}
