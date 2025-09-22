package co.edu.udistrital.controller;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ControlJuego controlJuego = new ControlJuego();
            new Thread(() -> {
                try {
                    Thread.sleep(500);
                    controlJuego.iniciar();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }
}