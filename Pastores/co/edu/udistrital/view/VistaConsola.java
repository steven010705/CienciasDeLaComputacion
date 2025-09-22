package co.edu.udistrital.view;

import co.edu.udistrital.model.MesaRedonda;
import co.edu.udistrital.model.Pastor;
import co.edu.udistrital.model.PilaDesposeidos;
import java.awt.*;
import java.awt.event.ActionListener;
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
        setTitle(" Juego de Pastores ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 245, 255));

        JPanel panelTurno = new JPanel();
        panelTurno.setBackground(new Color(200, 220, 255));
        panelTurno.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        labelTurno = new JLabel(" Esperando inicio del juego...");
        labelTurno.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
        labelTurno.setForeground(new Color(50, 50, 150));
        panelTurno.add(labelTurno);
        add(panelTurno, BorderLayout.NORTH);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelMesa = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(180, 220, 180));
                g2d.fillOval(40, 40, getWidth() - 80, getHeight() - 80);

                g2d.setStroke(new BasicStroke(6));
                g2d.setColor(new Color(100, 150, 100));
                g2d.drawOval(40, 40, getWidth() - 80, getHeight() - 80);
            }
        };
        panelMesa.setBackground(new Color(240, 255, 240));
        panelMesa.setPreferredSize(new Dimension(600, 600));
        panelMesa.setBorder(BorderFactory.createTitledBorder(" Mesa Redonda"));

        JScrollPane scrollMesa = new JScrollPane(panelMesa);
        scrollMesa.setPreferredSize(new Dimension(620, 620));
        panelPrincipal.add(scrollMesa, BorderLayout.CENTER);

        panelPila = new JPanel();
        panelPila.setLayout(new BoxLayout(panelPila, BoxLayout.Y_AXIS));
        panelPila.setBackground(new Color(255, 240, 240));
        panelPila.setPreferredSize(new Dimension(250, 600));
        panelPila.setBorder(BorderFactory.createTitledBorder(" Pila de Desposeídos"));

        JScrollPane scrollPila = new JScrollPane(panelPila);
        scrollPila.setPreferredSize(new Dimension(270, 620));
        panelPrincipal.add(scrollPila, BorderLayout.EAST);

        add(panelPrincipal, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(new Color(230, 230, 255));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        btnEliminar = new JButton("Eliminar");
        btnRescatar = new JButton("Rescatar");
        btnRobar = new JButton("Robar");

        JButton[] botones = {btnEliminar, btnRescatar, btnRobar};
        for (JButton b : botones) {
            b.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
            b.setBackground(new Color(200, 220, 255));
            b.setFocusPainted(false);
            b.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2, true));
        }

        panelBotones.add(btnEliminar);
        panelBotones.add(btnRescatar);
        panelBotones.add(btnRobar);
        add(panelBotones, BorderLayout.SOUTH);

        setMinimumSize(new Dimension(1100, 800));
        setLocationRelativeTo(null);
        pack();
    }

    public void mostrarMesa(MesaRedonda mesa) {
        if (mesa == null || mesa.getListaPastores() == null) return;

        List<Pastor> pastores = mesa.getListaPastores().toList();
        if (pastores.isEmpty()) return;

        SwingUtilities.invokeLater(() -> {
            panelMesa.removeAll();
            panelMesa.setLayout(null);

            int n = pastores.size();
            int panelWidth = panelMesa.getWidth();
            int panelHeight = panelMesa.getHeight();

            if (panelWidth == 0 || panelHeight == 0) {
                panelWidth = 600;
                panelHeight = 600;
            }

            int centerX = panelWidth / 2;
            int centerY = panelHeight / 2;
            int radius = Math.min(panelWidth, panelHeight) / 2 - 100;

            for (int i = 0; i < n; i++) {
                Pastor p = pastores.get(i);
                double angle = 2 * Math.PI * i / n;
                int x = (int) (centerX + radius * Math.cos(angle)) - 70;
                int y = (int) (centerY + radius * Math.sin(angle)) - 50;

                JLabel lbl = new JLabel("<html><center><b>" + p.getNombre() +
                        "</b><br> Religión: " + p.getReligion() + // Mostrar religión
                        "<br> Tesoro: " + p.getRiqueza() + "</center></html>");
                lbl.setBounds(x, y, 140, 80);
                lbl.setOpaque(true);
                lbl.setBackground(new Color(255, 255, 200));
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setVerticalAlignment(SwingConstants.CENTER);
                lbl.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true));
                lbl.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));

                panelMesa.add(lbl);
            }

            panelMesa.revalidate();
            panelMesa.repaint();

            if (primeraVez) {
                primeraVez = false;
                setVisible(true);
            }
        });
    }

    public void mostrarPilaDesposeidos(PilaDesposeidos pilaDesposeidos) {
        if (pilaDesposeidos == null) return;

        SwingUtilities.invokeLater(() -> {
            panelPila.removeAll();
            List<Pastor> pila = pilaDesposeidos.toList();

            if (pila.isEmpty()) {
                JLabel vacio = new JLabel(" Pila vacía");
                vacio.setFont(new Font("Comic Sans MS", Font.ITALIC, 16));
                vacio.setAlignmentX(Component.CENTER_ALIGNMENT);
                panelPila.add(vacio);
            } else {
                for (int i = pila.size() - 1; i >= 0; i--) {
                    Pastor p = pila.get(i);
                    JLabel lbl = new JLabel(" " + p.getNombre() +
                            " |  " + p.getReligion() + // Mostrar religión
                            " |  " + p.getRiqueza());
                    lbl.setOpaque(true);
                    lbl.setBackground(new Color(255, 230, 240));
                    lbl.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(Color.PINK, 2),
                            BorderFactory.createEmptyBorder(5, 10, 5, 10)
                    ));
                    lbl.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
                    lbl.setMaximumSize(new Dimension(220, 40));
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
                labelTurno.setText(" Turno " + numeroTurno + ": " + actual.getNombre() +
                        " |  " + actual.getReligion() + // Mostrar religión
                        " |  " + actual.getRiqueza() +
                        " |  Oficio: " + actual.getOficio());
            } else {
                labelTurno.setText(" Turno " + numeroTurno + ": Esperando pastor...");
            }
        });
    }

    public void mostrarGanador(Pastor ganador) {
        SwingUtilities.invokeLater(() -> {
            if (ganador != null) {
                JOptionPane.showMessageDialog(this,
                        " ¡El ganador es: " + ganador.getNombre() +
                                "\n Religión: " + ganador.getReligion() + // Mostrar religión
                                "\n Tesoro: " + ganador.getRiqueza() +  // ¡CORREGIDO: getRiqueza() en lugar de Riqueza()
                                "\n Oficio: " + ganador.getOficio(),
                        " ¡Juego terminado!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "El juego ha terminado, pero no hay ganador",
                        " Juego terminado", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    public void hacerVisible() {
        SwingUtilities.invokeLater(() -> setVisible(true));
    }

    public void addEliminarListener(ActionListener listener) {
        btnEliminar.addActionListener(listener);
    }

    public void addRescatarListener(ActionListener listener) {
        btnRescatar.addActionListener(listener);
    }

    public void addRobarListener(ActionListener listener) {
        btnRobar.addActionListener(listener);
    }
}