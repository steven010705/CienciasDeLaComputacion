package co.edu.udistrital.view;

import co.edu.udistrital.model.MesaRedonda;
import co.edu.udistrital.model.Pastor;
import co.edu.udistrital.model.PilaDesposeidos;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VistaConsola extends JFrame {

    private JPanel panelMesa;
    private JPanel panelPila;
    private JLabel labelTurno;
    private JButton btnEliminar;
    private JButton btnRescatar;
    private JButton btnRobar;

    public VistaConsola() {
        setTitle("Juego de Pastores");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Panel superior para mostrar el turno
        labelTurno = new JLabel("Turno: ");
        labelTurno.setFont(new Font("Arial", Font.BOLD, 18));
        add(labelTurno, BorderLayout.NORTH);

        // Panel central para la mesa redonda (círculo)
        panelMesa = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // El dibujo de la mesa se hace en mostrarMesa()
            }
        };
        panelMesa.setPreferredSize(new Dimension(500, 500));
        panelMesa.setBackground(Color.WHITE);

        // Panel derecho para la pila de desposeídos (columna)
        panelPila = new JPanel();
        panelPila.setLayout(new BoxLayout(panelPila, BoxLayout.Y_AXIS));
        panelPila.setBackground(new Color(240, 240, 240));
        panelPila.setPreferredSize(new Dimension(200, 500));
        panelPila.setBorder(BorderFactory.createTitledBorder("Pila de Desposeídos"));

        // Panel inferior para botones de acción
        JPanel panelBotones = new JPanel();
        btnEliminar = new JButton("Eliminar");
        btnRescatar = new JButton("Rescatar");
        btnRobar = new JButton("Robar");
        // Puedes deshabilitarlos si las acciones son automáticas
        btnEliminar.setEnabled(false);
        btnRescatar.setEnabled(false);
        btnRobar.setEnabled(false);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnRescatar);
        panelBotones.add(btnRobar);

        // Agregar paneles al frame
        add(panelMesa, BorderLayout.CENTER);
        add(panelPila, BorderLayout.EAST);
        add(panelBotones, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Muestra la mesa como un círculo de labels
    public void mostrarMesa(MesaRedonda mesa) {
        panelMesa.removeAll();
        panelMesa.repaint();

        // Obtener lista de pastores
        List<Pastor> pastores = mesa.getListaPastores().toList(); // Debes implementar este método
        int n = pastores.size();
        int radius = 200;
        int centerX = panelMesa.getWidth() / 2;
        int centerY = panelMesa.getHeight() / 2;

        for (int i = 0; i < n; i++) {
            Pastor p = pastores.get(i);
            double angle = 2 * Math.PI * i / n;
            int x = (int) (centerX + radius * Math.cos(angle)) - 40;
            int y = (int) (centerY + radius * Math.sin(angle)) - 20;
            JLabel lbl = new JLabel("<html>" + p.getNombre() + "<br>Ovejas: " + p.getOvejas() + "<br>Tesoro: " + p.getRiqueza() + "</html>");
            lbl.setBounds(x, y, 80, 40);
            lbl.setOpaque(true);
            lbl.setBackground(new Color(220, 240, 255));
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            lbl.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            panelMesa.add(lbl);
        }
        panelMesa.setLayout(null);
        panelMesa.revalidate();
        panelMesa.repaint();
    }

    // Muestra la pila como una columna de labels
    public void mostrarPilaDesposeidos(PilaDesposeidos pilaDesposeidos) {
        panelPila.removeAll();
        List<Pastor> pila = pilaDesposeidos.toList(); // Debes implementar este método
        for (int i = pila.size() - 1; i >= 0; i--) {
            Pastor p = pila.get(i);
            JLabel lbl = new JLabel(p.getNombre() + " | Ovejas: " + p.getOvejas() + " | Tesoro: " + p.getRiqueza());
            lbl.setOpaque(true);
            lbl.setBackground(new Color(255, 230, 230));
            lbl.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            panelPila.add(lbl);
        }
        panelPila.revalidate();
        panelPila.repaint();
    }

    // Actualiza el label del turno
    public void mostrarTurno(Pastor actual, int numeroTurno) {
        labelTurno.setText("Turno " + numeroTurno + ": " + actual.getNombre() +
                " | Ovejas: " + actual.getOvejas() +
                " | Tesoro: " + actual.getRiqueza() +
                " | Oficio: " + actual.getOficio());
    }

    // Muestra el ganador en un diálogo
    public void mostrarGanador(Pastor ganador) {
        JOptionPane.showMessageDialog(this,
                "El ganador es: " + ganador.getNombre() +
                        "\nOvejas: " + ganador.getOvejas() +
                        "\nTesoro: " + ganador.getRiqueza() +
                        "\nOficio: " + ganador.getOficio(),
                "¡Juego terminado!", JOptionPane.INFORMATION_MESSAGE);
    }
}
