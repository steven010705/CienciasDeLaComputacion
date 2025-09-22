package co.edu.udistrital.model;

import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Juego {

    // Atributos
    private MesaRedonda mesaRedonda;
    private PilaDesposeidos pilaDesposeidos;
    public int numeroTurno;
    private Random aleatorio;

    // Constructor
    public Juego() {
        mesaRedonda = new MesaRedonda();
        pilaDesposeidos = new PilaDesposeidos();
        numeroTurno = 0;
        aleatorio = new Random();
    }

    // Métodos
    public void iniciarJuego() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite el número de pastores: ");
        int n = scanner.nextInt();
        scanner.close();

        if (n < 1) {
            System.out.println("Número de pastores debe ser al menos 1. Se usará 1 por defecto.");
            n = 1;
        }

        numeroTurno = 1;
        System.out.println("Inicializando juego con " + n + " pastores...");

        String[] religiones = {"Cristiana", "Musulmana", "Hindú", "Budista", "Judía", "Sintoísta"}; // Array de Strings

        // Creación dinámica de n pastores
        for (int i = 1; i <= n; i++) {
            String nombre;
            if (i <= 26) {
                nombre = "Pastor " + (char)('A' + i - 1);
            } else {
                nombre = "Pastor " + i;
            }

            String oficio = "Oficio " + ((i - 1) % 3 + 1);
            String religion = religiones[aleatorio.nextInt(religiones.length)]; // ¡String, no int!
            int riqueza = aleatorio.nextInt(801) + 200; // Entre 200 y 1000

            // ¡Llamada al constructor con String religion!
            Pastor p = new Pastor(nombre, oficio, religion, riqueza);
            mesaRedonda.getListaPastores().agregarPastor(p);
            System.out.println("Creado: " + nombre + " (Oficio: " + oficio + ", Religión: " + religion + ", Riqueza: " + riqueza + ")");
        }

        // Configuración inicial: El juego comienza con el pastor más rico.
        if (!mesaRedonda.getListaPastores().estaVacia()) {
            mesaRedonda.setPastorActual(mesaRedonda.obtenerMasRico());
            System.out.println("El juego comienza con: " + mesaRedonda.getPastorActual().getNombre());
        }
    }

    public void ejecutarTurno() {
        if (juegoTerminado()) {
            return;
        }

        numeroTurno++;
        Pastor pastorEnTurno = mesaRedonda.getPastorActual();
        System.out.println("\n--- Turno " + numeroTurno + " ---");
        System.out.println("Es el turno de: " + pastorEnTurno.getNombre() + " (Religión: " + pastorEnTurno.getReligion() + ", Riqueza: " + pastorEnTurno.getRiqueza() + ")");

        NodoPastor nodoPastorEnTurno = buscarNodoPastor(pastorEnTurno);
        if (nodoPastorEnTurno == null) {
            System.out.println("Error: Pastor en turno no encontrado en la mesa. Saltando turno.");
            avanzarPastorActual();
            return;
        }

        Pastor pastorMasPobreEnMesa = null;
        if (!mesaRedonda.getListaPastores().estaVacia()) {
            pastorMasPobreEnMesa = mesaRedonda.getListaPastores().buscar().getPastor();
        }
        
        if (pastorMasPobreEnMesa != null && pastorEnTurno.equals(pastorMasPobreEnMesa)) {
            System.out.println(pastorEnTurno.getNombre() + " es el pastor más pobre de la mesa.");
            Pastor pastorMasRicoEnMesa = mesaRedonda.obtenerMasRico();
            
            if (pastorMasRicoEnMesa != null && !pastorEnTurno.equals(pastorMasRicoEnMesa)) {
                pastorEnTurno.robarRecursos(pastorMasRicoEnMesa);
                System.out.println(pastorEnTurno.getNombre() + " ha robado recursos a " + pastorMasRicoEnMesa.getNombre());
                reorganizarMesaDespuesDeAccion(nodoPastorEnTurno);
            } else {
                System.out.println(pastorEnTurno.getNombre() + " no pudo robar (es el único o no hay otro rico).");
            }
        } else {
            // Lógica para rescatar solo a pastores de la misma religión
            boolean puedeRescatar = !pilaDesposeidos.estaVacia();
            boolean quiereRescatar = puedeRescatar && aleatorio.nextBoolean();

            if (quiereRescatar) {
                // Buscar en la pila un pastor de la misma religión
                Pastor pastorARescatar = pilaDesposeidos.popPastorDeMismaReligion(pastorEnTurno.getReligion());
                if (pastorARescatar != null) {
                    pastorEnTurno.dividirRecursos(pastorARescatar);
                    mesaRedonda.getListaPastores().agregarPastor(pastorARescatar);
                    System.out.println(pastorEnTurno.getNombre() + " ha rescatado a " + pastorARescatar.getNombre());
                    reorganizarMesaDespuesDeAccion(nodoPastorEnTurno);
                } else {
                    System.out.println(pastorEnTurno.getNombre() + " quiso rescatar, pero no encontró un pastor de su misma religión en la pila.");
                    // Si no encontró uno de su misma religión, el turno continúa sin rescate
                    // y se podría considerar otra acción o simplemente avanzar el turno.
                    // Por ahora, avanzamos el turno.
                }
            } else {
                // Acción de eliminación: buscar pastor de diferente religión
                boolean sentidoDerecha = aleatorio.nextBoolean();
                int vecinos = 2;

                Pastor pastorAEliminar = mesaRedonda.obtenerMasPobreEntreVecinosDeDiferenteReligion(nodoPastorEnTurno, vecinos, sentidoDerecha);

                if (pastorAEliminar != null && !pastorAEliminar.equals(pastorEnTurno)) {
                    NodoPastor nodoAEliminar = buscarNodoPastor(pastorAEliminar);
                    if (nodoAEliminar != null) {
                        pastorAEliminar.transferirRecursos(pastorEnTurno, pastorAEliminar.getRiqueza()); // Solo riqueza
                        pilaDesposeidos.push(pastorAEliminar);
                        mesaRedonda.getListaPastores().eliminarPastor(nodoAEliminar);
                        System.out.println(pastorEnTurno.getNombre() + " ha eliminado a " + pastorAEliminar.getNombre());
                        reorganizarMesaDespuesDeAccion(nodoPastorEnTurno);
                    }
                } else {
                    System.out.println(pastorEnTurno.getNombre() + " no encontró un pastor de diferente religión para eliminar o no pudo eliminar.");
                }
            }
        }

        avanzarPastorActual();
    }

    public boolean juegoTerminado() {
        return mesaRedonda.getListaPastores().getSize() <= 1;
    }

    public Pastor obtenerGanador() {
        if (juegoTerminado() && mesaRedonda.getListaPastores().getSize() == 1) {
            return mesaRedonda.getListaPastores().toList().get(0);
        }
        return null;
    }

    private NodoPastor buscarNodoPastor(Pastor pastorBuscado) {
        if (mesaRedonda.getListaPastores().estaVacia()) {
            return null;
        }
        NodoPastor actual = mesaRedonda.getListaPastores().getCabeza();
        do {
            if (actual.getPastor().equals(pastorBuscado)) {
                return actual;
            }
            actual = actual.getSiguiente();
        } while (actual != mesaRedonda.getListaPastores().getCabeza());
        return null;
    }

    private void avanzarPastorActual() {
        if (mesaRedonda.getListaPastores().estaVacia()) {
            mesaRedonda.setPastorActual(null);
            return;
        }
        NodoPastor nodoPastorActual = buscarNodoPastor(mesaRedonda.getPastorActual());
        if (nodoPastorActual != null) {
            mesaRedonda.setPastorActual(nodoPastorActual.getSiguiente().getPastor());
        } else {
            mesaRedonda.setPastorActual(mesaRedonda.getListaPastores().getCabeza().getPastor());
        }
    }

    private void reorganizarMesaDespuesDeAccion(NodoPastor nodoPastorQueRealizoAccion) {
        System.out.println("Reorganizando la mesa (lógica de oficio/religión pendiente de implementación completa).");
    }

    // Getters
    public MesaRedonda getMesaRedonda() {
        return mesaRedonda;
    }

    public PilaDesposeidos getPilaDesposeidos() {
        return pilaDesposeidos;
    }

    public int getNumeroTurno() {
        return numeroTurno;
    }
}