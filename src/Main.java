import javax.swing.*;

/**
 * Punto di ingresso del programma.
 * Crea la finestra e avvia il gioco.
 *
 * Questa classe è già completa: non è necessario modificarla.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Asteroids");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);

            GameArea game = new GameArea();
            frame.add(game);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            game.requestFocusInWindow();
        });
    }
}
