// FileName: MultipleFiles/ControlJuego.java
package co.edu.udistrital.controller;

import co.edu.udistrital.model.Juego;
import co.edu.udistrital.model.Pastor;
import co.edu.udistrital.model.NodoPastor; // Importar NodoPastor
import co.edu.udistrital.view.VistaConsola;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane; // Para mostrar mensajes al usuario
import java.util.Random; // Para decisiones aleatorias en el controlador si es necesario

public class ControlJuego {
    private Juego juego;
    private VistaConsola vista;
    private Random aleatorio; // Para decisiones aleatorias en el controlador

    public ControlJuego() {
        juego = new Juego();
        vista = new VistaConsola();
        aleatorio = new Random(); // Inicializar Random

        // Adjuntar ActionListeners a los botones
        vista.addEliminarListener(new EliminarListener());
        vista.addRescatarListener(new RescatarListener());
        vista.addRobarListener(new RobarListener());
    }

    public void iniciar() {
        try {
            juego.iniciarJuego();
            
            Thread.sleep(100);
            
            javax.swing.SwingUtilities.invokeLater(() -> {
                vista.mostrarMesa(juego.getMesaRedonda());
                vista.mostrarPilaDesposeidos(juego.getPilaDesposeidos());
                vista.mostrarTurno(juego.getMesaRedonda().getPastorActual(), juego.getNumeroTurno());
                vista.hacerVisible();
            });

            // El bucle principal del juego ahora se controlará por las acciones de los botones
            // o por un botón "Siguiente Turno" si lo implementas.
            // Por ahora, eliminamos el bucle automático para que el usuario controle el flujo.
            // Si quieres que el juego avance automáticamente, puedes reintroducir un temporizador o un botón "Siguiente Turno".

            // Si el juego termina al inicio (menos de 2 pastores), mostrar ganador.
            if (juego.juegoTerminado()) {
                javax.swing.SwingUtilities.invokeLater(() -> {
                    vista.mostrarGanador(juego.obtenerGanador());
                });
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Juego interrumpido");
        }
    }

    // Método para actualizar la vista después de una acción
    private void actualizarVista() {
        javax.swing.SwingUtilities.invokeLater(() -> {
            vista.mostrarMesa(juego.getMesaRedonda());
            vista.mostrarPilaDesposeidos(juego.getPilaDesposeidos());
            vista.mostrarTurno(juego.getMesaRedonda().getPastorActual(), juego.getNumeroTurno());
            if (juego.juegoTerminado()) {
                vista.mostrarGanador(juego.obtenerGanador());
            }
        });
    }

    // Clase interna para el ActionListener del botón Eliminar
    class EliminarListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Lógica para eliminar un pastor
            Pastor pastorEnTurno = juego.getMesaRedonda().getPastorActual();
            if (pastorEnTurno == null) {
                JOptionPane.showMessageDialog(vista, "No hay pastor en turno.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Buscar el NodoPastor correspondiente al pastor actual en turno
            NodoPastor nodoPastorEnTurno = buscarNodoPastor(pastorEnTurno);
            if (nodoPastorEnTurno == null) {
                JOptionPane.showMessageDialog(vista, "Error: Pastor en turno no encontrado en la mesa.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Decidir dirección aleatoriamente (o podrías pedir al usuario que elija)
            boolean sentidoDerecha = aleatorio.nextBoolean();
            int n = 2; // Número fijo de vecinos a considerar

            Pastor pastorAEliminar = juego.getMesaRedonda().obtenerMasPobreEntreVecinos(nodoPastorEnTurno, n, sentidoDerecha);

            if (pastorAEliminar != null && !pastorAEliminar.equals(pastorEnTurno)) {
                NodoPastor nodoAEliminar = buscarNodoPastor(pastorAEliminar);
                if (nodoAEliminar != null) {
                    // Transferir recursos del eliminado al eliminador
                    pastorAEliminar.transferirRecursos(pastorEnTurno, pastorAEliminar.getOvejas(), pastorAEliminar.getRiqueza());
                    
                    // Añadir al pastor eliminado a la pila de desposeídos
                    juego.getPilaDesposeidos().push(pastorAEliminar);
                    
                    // Eliminar al pastor de la mesa redonda
                    juego.getMesaRedonda().getListaPastores().eliminarPastor(nodoAEliminar);
                    JOptionPane.showMessageDialog(vista, pastorEnTurno.getNombre() + " ha eliminado a " + pastorAEliminar.getNombre(), "Acción", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Reorganizar la mesa (marcador de posición)
                    reorganizarMesaDespuesDeAccion(nodoPastorEnTurno);
                    
                    // Avanzar al siguiente pastor y actualizar la vista
                    avanzarPastorActual();
                    actualizarVista();
                } else {
                    JOptionPane.showMessageDialog(vista, "No se encontró el nodo del pastor a eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(vista, pastorEnTurno.getNombre() + " no encontró un pastor para eliminar o no pudo eliminar.", "Información", JOptionPane.INFORMATION_MESSAGE);
                // Si no se pudo eliminar, el turno sigue siendo del mismo pastor o avanza.
                // Para este ejemplo, avanzamos el turno.
                avanzarPastorActual();
                actualizarVista();
            }
        }
    }

    // Clase interna para el ActionListener del botón Rescatar
    class RescatarListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Lógica para rescatar un pastor
            Pastor pastorEnTurno = juego.getMesaRedonda().getPastorActual();
            if (pastorEnTurno == null) {
                JOptionPane.showMessageDialog(vista, "No hay pastor en turno.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!juego.getPilaDesposeidos().estaVacia()) {
                Pastor pastorARescatar = juego.getPilaDesposeidos().pop();
                if (pastorARescatar != null) {
                    pastorEnTurno.dividirRecursos(pastorARescatar);
                    juego.getMesaRedonda().getListaPastores().agregarPastor(pastorARescatar);
                    JOptionPane.showMessageDialog(vista, pastorEnTurno.getNombre() + " ha rescatado a " + pastorARescatar.getNombre(), "Acción", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Reorganizar la mesa (marcador de posición)
                    NodoPastor nodoPastorEnTurno = buscarNodoPastor(pastorEnTurno);
                    if (nodoPastorEnTurno != null) {
                        reorganizarMesaDespuesDeAccion(nodoPastorEnTurno);
                    }
                    
                    // Avanzar al siguiente pastor y actualizar la vista
                    avanzarPastorActual();
                    actualizarVista();
                }
            } else {
                JOptionPane.showMessageDialog(vista, "La pila de desposeídos está vacía. No hay pastores para rescatar.", "Información", JOptionPane.INFORMATION_MESSAGE);
                // Si no se pudo rescatar, el turno sigue siendo del mismo pastor o avanza.
                // Para este ejemplo, avanzamos el turno.
                avanzarPastorActual();
                actualizarVista();
            }
        }
    }

    // Clase interna para el ActionListener del botón Robar
    class RobarListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Lógica para robar recursos
            Pastor pastorEnTurno = juego.getMesaRedonda().getPastorActual();
            if (pastorEnTurno == null) {
                JOptionPane.showMessageDialog(vista, "No hay pastor en turno.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Solo el pastor más pobre puede robar
            Pastor pastorMasPobreEnMesa = juego.getMesaRedonda().getListaPastores().buscar().getPastor();
            if (pastorEnTurno.equals(pastorMasPobreEnMesa)) {
                Pastor pastorMasRicoEnMesa = juego.getMesaRedonda().obtenerMasRico();
                
                if (pastorMasRicoEnMesa != null && !pastorEnTurno.equals(pastorMasRicoEnMesa)) {
                    pastorEnTurno.robarRecursos(pastorMasRicoEnMesa);
                    JOptionPane.showMessageDialog(vista, pastorEnTurno.getNombre() + " ha robado recursos a " + pastorMasRicoEnMesa.getNombre(), "Acción", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Reorganizar la mesa (marcador de posición)
                    NodoPastor nodoPastorEnTurno = buscarNodoPastor(pastorEnTurno);
                    if (nodoPastorEnTurno != null) {
                        reorganizarMesaDespuesDeAccion(nodoPastorEnTurno);
                    }
                    
                    // Avanzar al siguiente pastor y actualizar la vista
                    avanzarPastorActual();
                    actualizarVista();
                } else {
                    JOptionPane.showMessageDialog(vista, pastorEnTurno.getNombre() + " no pudo robar (es el único o no hay otro rico).", "Información", JOptionPane.INFORMATION_MESSAGE);
                    // Si no se pudo robar, el turno sigue siendo del mismo pastor o avanza.
                    // Para este ejemplo, avanzamos el turno.
                    avanzarPastorActual();
                    actualizarVista();
                }
            } else {
                JOptionPane.showMessageDialog(vista, pastorEnTurno.getNombre() + " no es el pastor más pobre y no puede robar.", "Información", JOptionPane.INFORMATION_MESSAGE);
                // Si no es el más pobre, no puede robar, el turno sigue siendo del mismo pastor o avanza.
                // Para este ejemplo, avanzamos el turno.
                avanzarPastorActual();
                actualizarVista();
            }
        }
    }

    // Métodos auxiliares copiados de Juego.java para que el controlador pueda usarlos
    // (o podrías hacerlos públicos en Juego si prefieres)
    private NodoPastor buscarNodoPastor(Pastor pastorBuscado) {
        if (juego.getMesaRedonda().getListaPastores().estaVacia()) {
            return null;
        }
        NodoPastor actual = juego.getMesaRedonda().getListaPastores().getCabeza();
        do {
            if (actual.getPastor().equals(pastorBuscado)) {
                return actual;
            }
            actual = actual.getSiguiente();
        } while (actual != juego.getMesaRedonda().getListaPastores().getCabeza());
        return null;
    }

    private void avanzarPastorActual() {
        // Incrementar el número de turno aquí, ya que cada acción representa un "turno" del usuario
        juego.numeroTurno++; 

        if (juego.getMesaRedonda().getListaPastores().estaVacia()) {
            juego.getMesaRedonda().setPastorActual(null);
            return;
        }
        NodoPastor nodoPastorActual = buscarNodoPastor(juego.getMesaRedonda().getPastorActual());
        if (nodoPastorActual != null) {
            juego.getMesaRedonda().setPastorActual(nodoPastorActual.getSiguiente().getPastor());
        } else {
            // Si el pastor actual fue eliminado o no se encontró, se establece la cabeza de la lista
            juego.getMesaRedonda().setPastorActual(juego.getMesaRedonda().getListaPastores().getCabeza().getPastor());
        }
    }

    private void reorganizarMesaDespuesDeAccion(NodoPastor nodoPastorQueRealizoAccion) {
        System.out.println("Reorganizando la mesa (lógica de oficio pendiente de implementación completa).");
        // Aquí iría la lógica compleja para reorganizar la mesa según las reglas de oficio.
        // Por ahora, es un marcador de posición.
    }

    public void finalizar() {
        if (vista != null) {
            vista.dispose();
        }
        System.exit(0);
    }
}
