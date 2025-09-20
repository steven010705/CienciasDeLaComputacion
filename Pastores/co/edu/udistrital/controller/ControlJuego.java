package co.edu.udistrital.controller;

import co.edu.udistrital.model.Juego;
import co.edu.udistrital.view.VistaConsola;

public class ControlJuego {
    private Juego juego;
    private VistaConsola vista;

    public ControlJuego() {
        juego = new Juego();
        vista = new VistaConsola();
    }

    public void iniciar() {
        juego.iniciarJuego();

        vista.mostrarMesa(juego.getMesaRedonda());
        vista.mostrarPilaDesposeidos(juego.getPilaDesposeidos());
        vista.mostrarTurno(juego.getMesaRedonda().getPastorActual(), juego.getNumeroTurno());
        
        while (!juego.juegoTerminado()) {
            juego.ejecutarTurno();

            vista.mostrarMesa(juego.getMesaRedonda());
            vista.mostrarPilaDesposeidos(juego.getPilaDesposeidos());
            vista.mostrarTurno(juego.getMesaRedonda().getPastorActual(), juego.getNumeroTurno());
        }
        vista.mostrarGanador(juego.obtenerGanador());
    }

    public void procesarTurno() {
        // Lógica para procesar el turno actual
        vista.mostrarMesa(juego.getMesaRedonda());
        vista.mostrarPilaDesposeidos(juego.getPilaDesposeidos());
        vista.mostrarTurno(juego.getMesaRedonda().getPastorActual(), juego.getNumeroTurno());
        try {
            Thread.sleep(1000); // 1 segundo entre turnos
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void finalizar() {
        // Lógica para finalizar el juego
        if (vista != null) {
            vista.dispose();
        }
        System.exit(0); // Opcional: termina la aplicación completamente
    }
}
