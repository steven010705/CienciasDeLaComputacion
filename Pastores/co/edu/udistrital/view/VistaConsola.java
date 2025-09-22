package co.edu.udistrital.view;

import co.edu.udistrital.model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class VistaConsola extends JFrame {

    private JPanel panelMesa, panelPila;
    private JLabel labelTurno;
    private JButton btnEliminar, btnRescatar, btnRobar;

    public VistaConsola() {
        setTitle("Juego de Pastores");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(255, 250, 240));

        JPanel panelTurno = new JPanel();
        panelTurno.setBackground(new Color(210, 240, 255));
        labelTurno = new JLabel("Esperando inicio del juego...");
        labelTurno.setFont(new Font("Comic Sans MS", Font.BOLD, 26));
        panelTurno.add(labelTurno);
        add(panelTurno, BorderLayout.NORTH);

        panelMesa = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight(), pad = 40;
                g2d.setColor(new Color(200, 255, 200));
                g2d.fillOval(pad, pad, w - 2 * pad, h - 2 * pad);
                g2d.setStroke(new BasicStroke(5));
                g2d.setColor(new Color(120, 170, 120));
                g2d.drawOval(pad, pad, w - 2 * pad, h - 2 * pad);
            }
        };
        panelMesa.setBorder(BorderFactory.createTitledBorder("Mesa Redonda"));

        panelPila = new JPanel();
        panelPila.setLayout(new BoxLayout(panelPila, BoxLayout.Y_AXIS));
        panelPila.setBackground(new Color(255, 230, 240));
        panelPila.setBorder(BorderFactory.createTitledBorder("Pila de Desposeídos"));

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.add(panelMesa, BorderLayout.CENTER);
        panelPrincipal.add(panelPila, BorderLayout.EAST);
        add(panelPrincipal, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(new Color(250, 240, 255));
        btnEliminar = new JButton("Eliminar");
        btnRescatar = new JButton("Rescatar");
        btnRobar = new JButton("Robar");
        for (JButton b : new JButton[]{btnEliminar, btnRescatar, btnRobar}) {
            b.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
            b.setBackground(new Color(220, 235, 250));
            b.setFocusPainted(false);
            b.setBorder(BorderFactory.createLineBorder(new Color(100, 120, 200), 2, true));
            panelBotones.add(b);
        }
        add(panelBotones, BorderLayout.SOUTH);
    }

    public int pedirNumeroPastores() {
        while (true) {
            String s = JOptionPane.showInputDialog(this, "Ingrese el número de pastores (mínimo 1):", "Configuración inicial", JOptionPane.QUESTION_MESSAGE);
            if (s == null) {
                return 0;
            
            }try {
                int n = Integer.parseInt(s);
                if (n > 0) {
                    return n;
            
                }} catch (Exception ignored) {
            }
            JOptionPane.showMessageDialog(this, "Número inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void mostrarMesa(MesaRedonda mesa) {
        if (mesa == null) {
            return;
        
        }List<Pastor> ps = mesa.getListaPastores().toList();
        if (ps.isEmpty()) {
            return;
        }
        SwingUtilities.invokeLater(() -> {
            panelMesa.removeAll();
            panelMesa.setLayout(null);
            int w = Math.max(panelMesa.getWidth(), 800), h = Math.max(panelMesa.getHeight(), 600);
            int cx = w / 2, cy = h / 2, lw = 85, lh = 50, n = ps.size();
            int r = (int) (Math.min(w, h) / 2 - 150 - Math.max(lw, lh) / 2); // 🔹 radio ajustado automáticamente
            for (int i = 0; i < n; i++) {
                Pastor p = ps.get(i);
                double ang = -Math.PI / 2 + 2 * Math.PI * i / n;
                int x = (int) (cx + r * Math.cos(ang) - lw / 2), y = (int) (cy + r * Math.sin(ang) - lh / 2);
                JLabel lbl = new JLabel("<html><center><b>" + p.getNombre() + "</b><br>" + p.getReligion() + "<br>$" + p.getRiqueza() + "</center></html>", SwingConstants.CENTER);
                lbl.setBounds(x, y, lw, lh);
                lbl.setOpaque(true);
                lbl.setBackground(new Color(255, 255, 220));
                lbl.setBorder(BorderFactory.createLineBorder(p.equals(mesa.getPastorActual()) ? Color.RED : new Color(180, 180, 180), 2, true));
                lbl.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
                panelMesa.add(lbl);
            }
            panelMesa.revalidate();
            panelMesa.repaint();
        });
    }

    public void mostrarPilaDesposeidos(PilaDesposeidos pila) {
        SwingUtilities.invokeLater(() -> {
            panelPila.removeAll();
            List<Pastor> l = pila.toList();
            if (l.isEmpty()) {
                JLabel v = new JLabel("Pila vacía");
                v.setFont(new Font("Comic Sans MS", Font.ITALIC, 16));
                v.setAlignmentX(CENTER_ALIGNMENT);
                panelPila.add(v);
            } else {
                for (Pastor p : l) {
                    JLabel lbl = new JLabel(" " + p.getNombre() + " | " + p.getReligion() + " | " + p.getRiqueza());
                    lbl.setOpaque(true);
                    lbl.setBackground(new Color(255, 240, 245));
                    lbl.setBorder(BorderFactory.createLineBorder(new Color(255, 200, 220), 2));
                    lbl.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
                    lbl.setMaximumSize(new Dimension(220, 40));
                    lbl.setAlignmentX(CENTER_ALIGNMENT);
                    panelPila.add(lbl);
                }
            }
            panelPila.revalidate();
            panelPila.repaint();
        });
    }

    public void mostrarTurno(Pastor a, int t) {
        SwingUtilities.invokeLater(() -> labelTurno.setText("Turno " + t + ": " + (a != null ? a.getNombre() + " | " + a.getReligion() + " | " + a.getRiqueza() + " | Oficio: " + a.getOficio() : "Esperando pastor...")));
    }

    public void mostrarGanador(Pastor g) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, g != null ? "¡El ganador es: " + g.getNombre() + "\nReligión: " + g.getReligion() + "\nTesoro: " + g.getRiqueza() + "\nOficio: " + g.getOficio() : "Sin ganador.", "Juego terminado", JOptionPane.INFORMATION_MESSAGE));
    }

    public void hacerVisible() {
        SwingUtilities.invokeLater(() -> setVisible(true));
    }

    public void addEliminarListener(ActionListener l) {
        btnEliminar.addActionListener(l);
    }

    public void addRescatarListener(ActionListener l) {
        btnRescatar.addActionListener(l);
    }

    public void addRobarListener(ActionListener l) {
        btnRobar.addActionListener(l);
    }
}
