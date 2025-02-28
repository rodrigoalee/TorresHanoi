import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TorresHanoi extends JFrame {
    private JTextField torresField, discosField, tiempoField;
    private JButton startButton, resetButton;
    private JPanel gamePanel;
    private PanelHanoi hanoiGamePanel;

    public TorresHanoi() {
        setTitle("Torres de Hanoi");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2));

        inputPanel.add(new JLabel("Número de Torres:"));
        torresField = new JTextField();
        inputPanel.add(torresField);

        inputPanel.add(new JLabel("Número de Discos:"));
        discosField = new JTextField();
        inputPanel.add(discosField);

        inputPanel.add(new JLabel("Tiempo"));
        tiempoField = new JTextField("50"); 
        inputPanel.add(tiempoField);

        startButton = new JButton("Iniciar Juego");
        inputPanel.add(startButton);

        resetButton = new JButton("Reiniciar Juego");
        resetButton.setEnabled(false);
        inputPanel.add(resetButton);

        gamePanel = new JPanel();
        gamePanel.setLayout(new BorderLayout());

        add(inputPanel, BorderLayout.NORTH);
        add(gamePanel, BorderLayout.CENTER);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (torresField.getText().isEmpty() || discosField.getText().isEmpty() || tiempoField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(TorresHanoi.this, "Por favor, ingresa valores en todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    int torres = Integer.parseInt(torresField.getText());
                    int discos = Integer.parseInt(discosField.getText());
                    int tiempo = Integer.parseInt(tiempoField.getText());

                    if (torres <= 0 || discos <= 0 || tiempo <= 0) {
                        JOptionPane.showMessageDialog(TorresHanoi.this, "Los valores deben ser mayores que 0.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    iniciarJuego(torres, discos, tiempo);
                    resetButton.setEnabled(true);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(TorresHanoi.this, "Por favor, ingresa valores numéricos válidos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int torres = Integer.parseInt(torresField.getText());
                    int discos = Integer.parseInt(discosField.getText());
                    int tiempo = Integer.parseInt(tiempoField.getText());
                    reiniciarJuego(torres, discos, tiempo);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(TorresHanoi.this, "Error al reiniciar el juego. Verifica los valores.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void iniciarJuego(int torres, int discos, int tiempo) {
        gamePanel.removeAll();
        hanoiGamePanel = new PanelHanoi(torres, discos, this, tiempo);
        gamePanel.add(hanoiGamePanel, BorderLayout.CENTER);
        gamePanel.revalidate();
        gamePanel.repaint();

        new Thread(() -> {
            hanoiGamePanel.resolverHanoi(
                discos,
                0,
                torres - 1,
                1
            );
        }).start();
    }

    private void reiniciarJuego(int torres, int discos, int tiempo) {
        iniciarJuego(torres, discos, tiempo);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("" + ex.getMessage());
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TorresHanoi().setVisible(true);
            }
        });
    }
}