package co.edu.udistrital.controller;

import co.edu.udistrital.model.Juego;
import co.edu.udistrital.view.VistaConsola;

public class ControlJuego {
    private Juego juego;
    private VistaConsola vista;

    public ControlJuego() {
        // Primero crear el modelo
        juego = new Juego();
        // Luego crear la vista
        vista = new VistaConsola();
    }

    public void iniciar() {
        try {
            // Iniciar el juego
            juego.iniciarJuego();
            
            // Pequeña pausa para que Swing se inicialice
            Thread.sleep(100);
            
            // Mostrar estado inicial EN EL EDT
            javax.swing.SwingUtilities.invokeLater(() -> {
                vista.mostrarMesa(juego.getMesaRedonda());
                vista.mostrarPilaDesposeidos(juego.getPilaDesposeidos());
                vista.mostrarTurno(juego.getMesaRedonda().getPastorActual(), juego.getNumeroTurno());
                vista.hacerVisible(); // Forzar visibilidad
            });

            // Bucle principal del juego
            while (!juego.juegoTerminado()) {
                Thread.sleep(1000); // Pausa antes del turno
                
                juego.ejecutarTurno();
                
                // Actualizar la vista en el EDT
                javax.swing.SwingUtilities.invokeLater(() -> {
                    vista.mostrarMesa(juego.getMesaRedonda());
                    vista.mostrarPilaDesposeidos(juego.getPilaDesposeidos());
                    vista.mostrarTurno(juego.getMesaRedonda().getPastorActual(), juego.getNumeroTurno());
                });
                
                Thread.sleep(2000); // Pausa para ver el resultado del turno
            }

            // Mostrar ganador
            javax.swing.SwingUtilities.invokeLater(() -> {
                vista.mostrarGanador(juego.obtenerGanador());
            });

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Juego interrumpido");
        }
    }

    public void finalizar() {
        if (vista != null) {
            vista.dispose();
        }
        System.exit(0);
    }
}