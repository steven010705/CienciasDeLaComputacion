package co.edu.udistrital.controller;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ControlJuego controlJuego = new ControlJuego();
            // El método iniciar() de ControlJuego ahora solo maneja la visibilidad y el inicio del hilo
            // La inicialización del juego (incluyendo pedir el número de pastores) se hace en el constructor de ControlJuego
            new Thread(() -> {
                try {
                    // Pequeña pausa para asegurar que la vista esté lista.
                    // Con las correcciones, esta pausa es menos crítica pero puede ayudar en sistemas lentos.
                    Thread.sleep(500); 
                    controlJuego.iniciar();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }
}