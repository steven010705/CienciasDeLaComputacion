package co.edu.udistrital.controller;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            ControlJuego controlJuego = new ControlJuego();
            controlJuego.iniciar();
        });
        
    }
}
