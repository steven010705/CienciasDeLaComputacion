package co.edu.udistrital.controller;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ControlJuego controlJuego = new ControlJuego();
            // Iniciar el juego en un hilo separado para no bloquear la EDT
            new Thread(() -> {
                try {
                    Thread.sleep(500); // Pequeña pausa para que Swing se inicialice
                    controlJuego.iniciar();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }
}