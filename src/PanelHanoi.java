import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

class PanelHanoi extends JPanel {
    private int torres;
    private int discos;
    private int[][] estadoTorres;
    private int torreOrigen = -1; 
    private int discoSeleccionado = -1; 
    private TorresHanoi hanoiGUI;
    private Color[] coloresDiscos; 
    private int tiempo;

    public PanelHanoi(int torres, int discos, TorresHanoi hanoiGUI, int tiempo) {
        this.torres = torres;
        this.discos = discos;
        this.hanoiGUI = hanoiGUI;
        this.tiempo = tiempo;
        this.estadoTorres = new int[torres][discos];
        this.coloresDiscos = generarColoresDiscos(); 
        inicializarTorres();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                manejarClic(e.getX());
            }
        });
    }

    private void inicializarTorres() {
        for (int i = 0; i < discos; i++) {
            estadoTorres[0][i] = discos - i;
        }
    }

    private Color[] generarColoresDiscos() {
        Color[] colores = new Color[discos];
        for (int i = 0; i < discos; i++) {
            colores[i] = new Color(
                (int) (Math.random() * 256),
                (int) (Math.random() * 256),
                (int) (Math.random() * 256)
            );
        }
        return colores;
    }

    private void manejarClic(int x) {
        int anchoTorre = getWidth() / torres;
        int torreClickeada = x / anchoTorre; 

        if (torreOrigen == -1) {
            torreOrigen = torreClickeada;
            discoSeleccionado = obtenerDiscoSuperior(torreOrigen);
        } else {
            int torreDestino = torreClickeada;
            if (torreOrigen != torreDestino) {
                moverDisco(torreOrigen, torreDestino);
            }
            torreOrigen = -1; 
            discoSeleccionado = -1;
        }
        repaint(); 
    }

    private void moverDisco(int torreOrigen, int torreDestino) {
        int discoOrigen = obtenerDiscoSuperior(torreOrigen);
        int discoDestino = obtenerDiscoSuperior(torreDestino);

        if (discoOrigen == -1) {
            return;
        }

        if (discoDestino != -1 && estadoTorres[torreOrigen][discoOrigen] > estadoTorres[torreDestino][discoDestino]) {
            JOptionPane.showMessageDialog(this, "Movimiento inválido: No puedes colocar un disco más grande sobre uno más pequeño.");
            return;
        }

        estadoTorres[torreDestino][discoDestino + 1] = estadoTorres[torreOrigen][discoOrigen];
        estadoTorres[torreOrigen][discoOrigen] = 0;
    }

    private int obtenerDiscoSuperior(int torre) {
        for (int i = discos - 1; i >= 0; i--) {
            if (estadoTorres[torre][i] != 0) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        dibujarTorres(g);
        if (discoSeleccionado != -1) {
            resaltarDiscoSeleccionado(g);
        }
    }

    private void dibujarTorres(Graphics g) {
        int anchoTorre = getWidth() / torres;
        int altoDisco = getHeight() / (discos + 1);

        for (int i = 0; i < torres; i++) {
            int x = i * anchoTorre + anchoTorre / 2;
            g.drawLine(x, 0, x, getHeight());

            for (int j = 0; j < discos; j++) {
                if (estadoTorres[i][j] != 0) {
                    int anchoDisco = estadoTorres[i][j] * anchoTorre / (discos + 1);
                    int y = getHeight() - (j + 1) * altoDisco;

                    g.setColor(coloresDiscos[estadoTorres[i][j] - 1]); 
                    g.fillOval(x - anchoDisco / 2, y, anchoDisco, altoDisco / 2); 

                    g.setColor(Color.WHITE);
                    g.drawOval(x - anchoDisco / 2, y, anchoDisco, altoDisco / 2);
                }
            }
        }
    }

    private void resaltarDiscoSeleccionado(Graphics g) {
        int anchoTorre = getWidth() / torres;
        int altoDisco = getHeight() / (discos + 1);
        int x = torreOrigen * anchoTorre + anchoTorre / 2;
        int y = getHeight() - (discoSeleccionado + 1) * altoDisco;
        int anchoDisco = estadoTorres[torreOrigen][discoSeleccionado] * anchoTorre / (discos + 1);

        g.setColor(Color.BLUE);
        g.drawOval(x - anchoDisco / 2, y, anchoDisco, altoDisco / 2);
    }

    public void resolverHanoi(int discos, int torreOrigen, int torreDestino, int torreAuxiliar) {
        if (discos == 1) {
            moverDisco(torreOrigen, torreDestino);
            repaint();
            try {
                Thread.sleep(tiempo);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            resolverHanoi(discos - 1, torreOrigen, torreAuxiliar, torreDestino);
            moverDisco(torreOrigen, torreDestino);
            repaint();
            try {
                Thread.sleep(tiempo);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            resolverHanoi(discos - 1, torreAuxiliar, torreDestino, torreOrigen);
        }
    }
}