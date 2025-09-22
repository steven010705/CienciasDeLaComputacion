package co.edu.udistrital.controller;

import co.edu.udistrital.model.Juego;
import co.edu.udistrital.model.NodoPastor;
import co.edu.udistrital.model.Pastor;
import co.edu.udistrital.view.VistaConsola;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.JOptionPane;

public class ControlJuego {
    private Juego juego;
    private VistaConsola vista;
    private Random aleatorio;

    public ControlJuego() {
        vista = new VistaConsola(); // Inicializar la vista primero para poder usarla
        int numPastores = vista.pedirNumeroPastores(); // Solicitar número de pastores a través de la vista

        if (numPastores == 0) { // Si el usuario cancela o ingresa 0 (manejo de cancelación)
            JOptionPane.showMessageDialog(vista, "El juego ha sido cancelado.", "Juego Terminado", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0); // Terminar la aplicación si el usuario cancela
        }

        juego = new Juego(numPastores); // Pasar el número de pastores al constructor de Juego
        aleatorio = new Random();

        // Añadir listeners a los botones de la vista
        vista.addEliminarListener(new EliminarListener());
        vista.addRescatarListener(new RescatarListener());
        vista.addRobarListener(new RobarListener());
    }

    public void iniciar() {
        // El método iniciar() ahora solo se encarga de mostrar la vista y el estado inicial del juego
        // La lógica de inicialización de pastores ya se hizo en el constructor de Juego
        javax.swing.SwingUtilities.invokeLater(() -> {
            vista.hacerVisible(); // Primero haz la ventana visible
            vista.pack();         // Asegura que los componentes tengan sus tamaños preferidos
            vista.revalidate();   // Valida el layout para que las dimensiones se calculen

            // Ahora sí, muestra la mesa y la pila con dimensiones correctas
            vista.mostrarMesa(juego.getMesaRedonda());
            vista.mostrarPilaDesposeidos(juego.getPilaDesposeidos());
            vista.mostrarTurno(juego.getMesaRedonda().getPastorActual(), juego.getNumeroTurno());
        });

        // Verificar si el juego termina inmediatamente (ej. si se inició con 1 pastor)
        if (juego.juegoTerminado()) {
            javax.swing.SwingUtilities.invokeLater(() -> {
                vista.mostrarGanador(juego.obtenerGanador());
            });
        }
    }

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

    // Listener para la acción de Eliminar
    class EliminarListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Pastor pastorEnTurno = juego.getMesaRedonda().getPastorActual();
            if (pastorEnTurno == null) {
                JOptionPane.showMessageDialog(vista, "No hay pastor en turno.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            NodoPastor nodoPastorEnTurno = buscarNodoPastor(pastorEnTurno);
            if (nodoPastorEnTurno == null) {
                JOptionPane.showMessageDialog(vista, "Error: Pastor en turno no encontrado en la mesa.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean sentidoDerecha = aleatorio.nextBoolean(); // Decide aleatoriamente la dirección
            int n = 2; // Número de vecinos a considerar

            // Obtener el pastor más pobre de diferente religión entre los vecinos
            Pastor pastorAEliminar = juego.getMesaRedonda().obtenerMasPobreEntreVecinosDeDiferenteReligion(nodoPastorEnTurno, n, sentidoDerecha);

            if (pastorAEliminar != null && !pastorAEliminar.equals(pastorEnTurno)) {
                NodoPastor nodoAEliminar = buscarNodoPastor(pastorAEliminar);
                if (nodoAEliminar != null) {
                    // Transferir solo riqueza del eliminado al que elimina
                    pastorAEliminar.transferirRecursos(pastorEnTurno, pastorAEliminar.getRiqueza());
                    
                    juego.getPilaDesposeidos().push(pastorAEliminar); // Mover a la pila de desposeídos
                    
                    juego.getMesaRedonda().getListaPastores().eliminarPastor(nodoAEliminar); // Eliminar de la mesa
                    JOptionPane.showMessageDialog(vista, pastorEnTurno.getNombre() + " ha eliminado a " + pastorAEliminar.getNombre() + " por ser de diferente religión.", "Acción", JOptionPane.INFORMATION_MESSAGE);
                    
                    reorganizarMesaDespuesDeAccion(nodoPastorEnTurno); // Reorganizar la mesa
                    
                    avanzarPastorActual(); // Avanzar al siguiente turno
                    actualizarVista(); // Actualizar la interfaz
                } else {
                    JOptionPane.showMessageDialog(vista, "No se encontró el nodo del pastor a eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(vista, pastorEnTurno.getNombre() + " no encontró un pastor de diferente religión para eliminar o no pudo eliminar.", "Información", JOptionPane.INFORMATION_MESSAGE);
                avanzarPastorActual();
                actualizarVista();
            }
        }
    }

    // Listener para la acción de Rescatar
    class RescatarListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Pastor pastorEnTurno = juego.getMesaRedonda().getPastorActual();
            if (pastorEnTurno == null) {
                JOptionPane.showMessageDialog(vista, "No hay pastor en turno.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Solo se puede rescatar a pastores de la misma religión
            Pastor pastorARescatar = juego.getPilaDesposeidos().popPastorDeMismaReligion(pastorEnTurno.getReligion());

            if (pastorARescatar != null) {
                pastorEnTurno.dividirRecursos(pastorARescatar); // Dividir recursos con el rescatado
                juego.getMesaRedonda().getListaPastores().agregarPastor(pastorARescatar); // Agregar de nuevo a la mesa
                JOptionPane.showMessageDialog(vista, pastorEnTurno.getNombre() + " ha rescatado a " + pastorARescatar.getNombre() + " (de su misma religión).", "Acción", JOptionPane.INFORMATION_MESSAGE);
                
                NodoPastor nodoPastorEnTurno = buscarNodoPastor(pastorEnTurno);
                if (nodoPastorEnTurno != null) {
                    reorganizarMesaDespuesDeAccion(nodoPastorEnTurno); // Reorganizar la mesa
                }
                
                avanzarPastorActual(); // Avanzar al siguiente turno
                actualizarVista(); // Actualizar la interfaz
            } else {
                JOptionPane.showMessageDialog(vista, "La pila de desposeídos no contiene pastores de la misma religión que " + pastorEnTurno.getNombre() + " para rescatar.", "Información", JOptionPane.INFORMATION_MESSAGE);
                avanzarPastorActual();
                actualizarVista();
            }
        }
    }

    // Listener para la acción de Robar
    class RobarListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
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
                    pastorEnTurno.robarRecursos(pastorMasRicoEnMesa); // Robar recursos al más rico
                    JOptionPane.showMessageDialog(vista, pastorEnTurno.getNombre() + " ha robado recursos a " + pastorMasRicoEnMesa.getNombre(), "Acción", JOptionPane.INFORMATION_MESSAGE);
                    
                    NodoPastor nodoPastorEnTurno = buscarNodoPastor(pastorEnTurno);
                    if (nodoPastorEnTurno != null) {
                        reorganizarMesaDespuesDeAccion(nodoPastorEnTurno); // Reorganizar la mesa
                    }
                    
                    avanzarPastorActual(); // Avanzar al siguiente turno
                    actualizarVista(); // Actualizar la interfaz
                } else {
                    JOptionPane.showMessageDialog(vista, pastorEnTurno.getNombre() + " no pudo robar (es el único o no hay otro rico).", "Información", JOptionPane.INFORMATION_MESSAGE);
                    avanzarPastorActual();
                    actualizarVista();
                }
            } else {
                JOptionPane.showMessageDialog(vista, pastorEnTurno.getNombre() + " no es el pastor más pobre y no puede robar.", "Información", JOptionPane.INFORMATION_MESSAGE);
                avanzarPastorActual();
                actualizarVista();
            }
        }
    }

    // Método auxiliar para buscar un NodoPastor dado un Pastor
    private NodoPastor buscarNodoPastor(Pastor pastorBuscado) {
        if (juego.getMesaRedonda().getListaPastores().estaVacia()) {
            return null;
        }
        NodoPastor actual = juego.getMesaRedonda().getListaPastores().getCabeza();
        if (actual == null) return null; // Manejo de caso de lista vacía después de obtener cabeza
        do {
            if (actual.getPastor().equals(pastorBuscado)) {
                return actual;
            }
            actual = actual.getSiguiente();
        } while (actual != juego.getMesaRedonda().getListaPastores().getCabeza());
        return null;
    }

    // Método para avanzar al siguiente pastor en el turno
    private void avanzarPastorActual() {
        juego.numeroTurno++; // Incrementar el número de turno

        if (juego.getMesaRedonda().getListaPastores().estaVacia()) {
            juego.getMesaRedonda().setPastorActual(null); // Si no hay pastores, no hay pastor actual
            return;
        }
        NodoPastor nodoPastorActual = buscarNodoPastor(juego.getMesaRedonda().getPastorActual());
        if (nodoPastorActual != null) {
            juego.getMesaRedonda().setPastorActual(nodoPastorActual.getSiguiente().getPastor());
        } else {
            // Si el pastor actual no se encontró (ej. fue eliminado), se toma la cabeza de la lista
            juego.getMesaRedonda().setPastorActual(juego.getMesaRedonda().getListaPastores().getCabeza().getPastor());
        }
    }

    // Método de marcador de posición para la reorganización de la mesa
    private void reorganizarMesaDespuesDeAccion(NodoPastor nodoPastorQueRealizoAccion) {
        // Lógica de reorganización de la mesa según reglas de oficio/religión
        // Esta parte es compleja y se mantiene como un marcador de posición.
        // Implicaría reordenar los nodos en la ListaCircularDoble.
        System.out.println("Reorganizando la mesa (lógica de oficio/religión pendiente de implementación completa).");
    }

    // Método para finalizar el juego y cerrar la ventana
    public void finalizar() {
        if (vista != null) {
            vista.dispose();
        }
        System.exit(0);
    }
}