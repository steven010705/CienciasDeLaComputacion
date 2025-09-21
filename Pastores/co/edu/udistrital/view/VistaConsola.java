package co.edu.udistrital.view;

import co.edu.udistrital.model.MesaRedonda;
import co.edu.udistrital.model.Pastor;
import co.edu.udistrital.model.PilaDesposeidos;
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class VistaConsola extends JFrame {

    private JPanel panelMesa;
    private JPanel panelPila;
    private JLabel labelTurno;
    private JButton btnEliminar;
    private JButton btnRescatar;
    private JButton btnRobar;
    private boolean primeraVez = true;

    public VistaConsola() {
        setTitle("Juego de Pastores");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Configurar layout principal
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);

        // Panel superior para el turno
        JPanel panelTurno = new JPanel();
        panelTurno.setBackground(new Color(240, 240, 240));
        panelTurno.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        labelTurno = new JLabel("Esperando inicio del juego...");
        labelTurno.setFont(new Font("Arial", Font.BOLD, 16));
        panelTurno.add(labelTurno);
        add(panelTurno, BorderLayout.NORTH);

        // Panel principal central para mesa y pila
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel de la mesa (con scroll por si hay muchos pastores)
        panelMesa = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Dibujar círculo de la mesa
                g.setColor(new Color(200, 200, 200));
                g.drawOval(50, 50, getWidth() - 100, getHeight() - 100);
            }
        };
        panelMesa.setBackground(Color.WHITE);
        panelMesa.setPreferredSize(new Dimension(400, 400));
        panelMesa.setBorder(BorderFactory.createTitledBorder("Mesa Redonda"));
        
        JScrollPane scrollMesa = new JScrollPane(panelMesa);
        scrollMesa.setPreferredSize(new Dimension(420, 420));
        panelPrincipal.add(scrollMesa, BorderLayout.CENTER);

        // Panel de la pila de desposeídos
        panelPila = new JPanel();
        panelPila.setLayout(new BoxLayout(panelPila, BoxLayout.Y_AXIS));
        panelPila.setBackground(new Color(240, 240, 240));
        panelPila.setPreferredSize(new Dimension(200, 400));
        panelPila.setBorder(BorderFactory.createTitledBorder("Pila de Desposeídos"));
        
        JScrollPane scrollPila = new JScrollPane(panelPila);
        scrollPila.setPreferredSize(new Dimension(220, 420));
        panelPrincipal.add(scrollPila, BorderLayout.EAST);

        add(panelPrincipal, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btnEliminar = new JButton("Eliminar");
        btnRescatar = new JButton("Rescatar");
        btnRobar = new JButton("Robar");
        
        // Deshabilitar botones por ahora
        btnEliminar.setEnabled(false);
        btnRescatar.setEnabled(false);
        btnRobar.setEnabled(false);
        
        panelBotones.add(btnEliminar);
        panelBotones.add(btnRescatar);
        panelBotones.add(btnRobar);
        add(panelBotones, BorderLayout.SOUTH);

        // Configurar tamaño y mostrar
        pack();
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 600));
    }

    public void mostrarMesa(MesaRedonda mesa) {
        if (mesa == null || mesa.getListaPastores() == null) {
            System.out.println("Mesa o lista de pastores es null");
            return;
        }

        List<Pastor> pastores = mesa.getListaPastores().toList();
        if (pastores.isEmpty()) {
            System.out.println("Lista de pastores está vacía");
            return;
        }

        // Usar SwingUtilities para asegurar que se ejecute en el EDT
        SwingUtilities.invokeLater(() -> {
            panelMesa.removeAll();
            panelMesa.setLayout(null); // Layout absoluto para posicionamiento preciso

            int n = pastores.size();
            int panelWidth = panelMesa.getWidth();
            int panelHeight = panelMesa.getHeight();
            
            // Si el panel aún no tiene tamaño (primera vez), usar tamaño preferido
            if (panelWidth == 0 || panelHeight == 0) {
                panelWidth = 400;
                panelHeight = 400;
            }

            int centerX = panelWidth / 2;
            int centerY = panelHeight / 2;
            int radius = Math.min(panelWidth, panelHeight) / 2 - 60;

            for (int i = 0; i < n; i++) {
                Pastor p = pastores.get(i);
                double angle = 2 * Math.PI * i / n;
                int x = (int) (centerX + radius * Math.cos(angle)) - 50;
                int y = (int) (centerY + radius * Math.sin(angle)) - 25;
                
                JLabel lbl = new JLabel("<html><center>" + p.getNombre() + 
                                       "<br>Ovejas: " + p.getOvejas() + 
                                       "<br>Tesoro: " + p.getRiqueza() + "</center></html>");
                lbl.setBounds(x, y, 100, 50);
                lbl.setOpaque(true);
                lbl.setBackground(new Color(220, 240, 255));
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setVerticalAlignment(SwingConstants.CENTER);
                lbl.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                lbl.setFont(new Font("Arial", Font.PLAIN, 10));
                
                panelMesa.add(lbl);
            }

            panelMesa.revalidate();
            panelMesa.repaint();
            
            if (primeraVez) {
                primeraVez = false;
                setVisible(true); // Hacer visible solo después de tener contenido
            }
        });
    }

    public void mostrarPilaDesposeidos(PilaDesposeidos pilaDesposeidos) {
        if (pilaDesposeidos == null) {
            System.out.println("Pila de desposeídos es null");
            return;
        }

        SwingUtilities.invokeLater(() -> {
            panelPila.removeAll();
            
            List<Pastor> pila = pilaDesposeidos.toList();
            if (pila.isEmpty()) {
                JLabel vacio = new JLabel("Pila vacía");
                vacio.setAlignmentX(Component.CENTER_ALIGNMENT);
                panelPila.add(vacio);
            } else {
                for (int i = pila.size() - 1; i >= 0; i--) {
                    Pastor p = pila.get(i);
                    JLabel lbl = new JLabel(p.getNombre() + " | Ovejas: " + p.getOvejas() + " | Tesoro: " + p.getRiqueza());
                    lbl.setOpaque(true);
                    lbl.setBackground(new Color(255, 230, 230));
                    lbl.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.GRAY, 1),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)
                    ));
                    lbl.setMaximumSize(new Dimension(190, 30));
                    panelPila.add(lbl);
                }
            }
            
            panelPila.revalidate();
            panelPila.repaint();
        });
    }

    public void mostrarTurno(Pastor actual, int numeroTurno) {
        SwingUtilities.invokeLater(() -> {
            if (actual != null) {
                labelTurno.setText("Turno " + numeroTurno + ": " + actual.getNombre() +
                        " | Ovejas: " + actual.getOvejas() +
                        " | Tesoro: " + actual.getRiqueza() +
                        " | Oficio: " + actual.getOficio());
            } else {
                labelTurno.setText("Turno " + numeroTurno + ": Esperando pastor...");
            }
        });
    }

    public void mostrarGanador(Pastor ganador) {
        SwingUtilities.invokeLater(() -> {
            if (ganador != null) {
                JOptionPane.showMessageDialog(this,
                        "¡El ganador es: " + ganador.getNombre() +
                                "\nOvejas: " + ganador.getOvejas() +
                                "\nTesoro: " + ganador.getRiqueza() +
                                "\nOficio: " + ganador.getOficio(),
                        "¡Juego terminado!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "El juego ha terminado, pero no hay ganador",
                        "Juego terminado", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    // Método para forzar la visibilidad cuando esté listo
    public void hacerVisible() {
        SwingUtilities.invokeLater(() -> {
            setVisible(true);
        });
    }
}