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
        juego = new Juego();
        vista = new VistaConsola();
        aleatorio = new Random();

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

            boolean sentidoDerecha = aleatorio.nextBoolean();
            int n = 2;

            // Usar el nuevo método para eliminar por religión
            Pastor pastorAEliminar = juego.getMesaRedonda().obtenerMasPobreEntreVecinosDeDiferenteReligion(nodoPastorEnTurno, n, sentidoDerecha);

            if (pastorAEliminar != null && !pastorAEliminar.equals(pastorEnTurno)) {
                NodoPastor nodoAEliminar = buscarNodoPastor(pastorAEliminar);
                if (nodoAEliminar != null) {
                    // Transferir solo riqueza
                    pastorAEliminar.transferirRecursos(pastorEnTurno, pastorAEliminar.getRiqueza());
                    
                    juego.getPilaDesposeidos().push(pastorAEliminar);
                    
                    juego.getMesaRedonda().getListaPastores().eliminarPastor(nodoAEliminar);
                    JOptionPane.showMessageDialog(vista, pastorEnTurno.getNombre() + " ha eliminado a " + pastorAEliminar.getNombre() + " por ser de diferente religión.", "Acción", JOptionPane.INFORMATION_MESSAGE);
                    
                    reorganizarMesaDespuesDeAccion(nodoPastorEnTurno);
                    
                    avanzarPastorActual();
                    actualizarVista();
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

    class RescatarListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Pastor pastorEnTurno = juego.getMesaRedonda().getPastorActual();
            if (pastorEnTurno == null) {
                JOptionPane.showMessageDialog(vista, "No hay pastor en turno.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Modificación: solo se puede rescatar a pastores de la misma religión
            Pastor pastorARescatar = juego.getPilaDesposeidos().popPastorDeMismaReligion(pastorEnTurno.getReligion());

            if (pastorARescatar != null) {
                pastorEnTurno.dividirRecursos(pastorARescatar);
                juego.getMesaRedonda().getListaPastores().agregarPastor(pastorARescatar);
                JOptionPane.showMessageDialog(vista, pastorEnTurno.getNombre() + " ha rescatado a " + pastorARescatar.getNombre() + " (de su misma religión).", "Acción", JOptionPane.INFORMATION_MESSAGE);
                
                NodoPastor nodoPastorEnTurno = buscarNodoPastor(pastorEnTurno);
                if (nodoPastorEnTurno != null) {
                    reorganizarMesaDespuesDeAccion(nodoPastorEnTurno);
                }
                
                avanzarPastorActual();
                actualizarVista();
            } else {
                JOptionPane.showMessageDialog(vista, "La pila de desposeídos no contiene pastores de la misma religión que " + pastorEnTurno.getNombre() + " para rescatar.", "Información", JOptionPane.INFORMATION_MESSAGE);
                avanzarPastorActual();
                actualizarVista();
            }
        }
    }

    class RobarListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Pastor pastorEnTurno = juego.getMesaRedonda().getPastorActual();
            if (pastorEnTurno == null) {
                JOptionPane.showMessageDialog(vista, "No hay pastor en turno.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Pastor pastorMasPobreEnMesa = juego.getMesaRedonda().getListaPastores().buscar().getPastor();
            if (pastorEnTurno.equals(pastorMasPobreEnMesa)) {
                Pastor pastorMasRicoEnMesa = juego.getMesaRedonda().obtenerMasRico();
                
                if (pastorMasRicoEnMesa != null && !pastorEnTurno.equals(pastorMasRicoEnMesa)) {
                    pastorEnTurno.robarRecursos(pastorMasRicoEnMesa);
                    JOptionPane.showMessageDialog(vista, pastorEnTurno.getNombre() + " ha robado recursos a " + pastorMasRicoEnMesa.getNombre(), "Acción", JOptionPane.INFORMATION_MESSAGE);
                    
                    NodoPastor nodoPastorEnTurno = buscarNodoPastor(pastorEnTurno);
                    if (nodoPastorEnTurno != null) {
                        reorganizarMesaDespuesDeAccion(nodoPastorEnTurno);
                    }
                    
                    avanzarPastorActual();
                    actualizarVista();
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
        juego.numeroTurno++; 

        if (juego.getMesaRedonda().getListaPastores().estaVacia()) {
            juego.getMesaRedonda().setPastorActual(null);
            return;
        }
        NodoPastor nodoPastorActual = buscarNodoPastor(juego.getMesaRedonda().getPastorActual());
        if (nodoPastorActual != null) {
            juego.getMesaRedonda().setPastorActual(nodoPastorActual.getSiguiente().getPastor());
        } else {
            juego.getMesaRedonda().setPastorActual(juego.getMesaRedonda().getListaPastores().getCabeza().getPastor());
        }
    }

    private void reorganizarMesaDespuesDeAccion(NodoPastor nodoPastorQueRealizoAccion) {
        System.out.println("Reorganizando la mesa (lógica de oficio/religión pendiente de implementación completa).");
    }

    public void finalizar() {
        if (vista != null) {
            vista.dispose();
        }
        System.exit(0);
    }
}