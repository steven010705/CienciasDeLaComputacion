// FileName: MultipleFiles/Juego.java
package co.edu.udistrital.model;

import java.util.List;
import java.util.Random; // Para simular decisiones aleatorias

public class Juego {

    // Atributos
    private MesaRedonda mesaRedonda;
    private PilaDesposeidos pilaDesposeidos;
    private int numeroTurno;
    private Random aleatorio; // Para simular decisiones aleatorias

    // Constructor
    public Juego() {
        mesaRedonda = new MesaRedonda();
        pilaDesposeidos = new PilaDesposeidos();
        numeroTurno = 0;
        aleatorio = new Random(); // Inicializa el generador de números aleatorios
    }

    // Métodos
    public void iniciarJuego() {
        numeroTurno = 1;
        // Creación de pastores iniciales
        Pastor p1 = new Pastor("Pastor A", "Oficio 1", 100, 500);
        Pastor p2 = new Pastor("Pastor B", "Oficio 2", 150, 800);
        Pastor p3 = new Pastor("Pastor C", "Oficio 1", 80, 300);
        Pastor p4 = new Pastor("Pastor D", "Oficio 3", 200, 1000);
        Pastor p5 = new Pastor("Pastor E", "Oficio 2", 120, 600); // Añadido para más variedad

        // Agregando pastores a la mesa redonda
        mesaRedonda.getListaPastores().agregarPastor(p1);
        mesaRedonda.getListaPastores().agregarPastor(p2);
        mesaRedonda.getListaPastores().agregarPastor(p3);
        mesaRedonda.getListaPastores().agregarPastor(p4);
        mesaRedonda.getListaPastores().agregarPastor(p5);

        // Configuración inicial: El juego comienza con el pastor más rico.
        if (!mesaRedonda.getListaPastores().estaVacia()) {
            mesaRedonda.setPastorActual(mesaRedonda.obtenerMasRico());
        }
    }

    public void ejecutarTurno() {
        // Si el juego ha terminado, no se ejecuta el turno
        if (juegoTerminado()) {
            return;
        }

        numeroTurno++;
        Pastor pastorEnTurno = mesaRedonda.getPastorActual();
        System.out.println("\n--- Turno " + numeroTurno + " ---");
        System.out.println("Es el turno de: " + pastorEnTurno.getNombre() + " (Ovejas: " + pastorEnTurno.getOvejas() + ", Riqueza: " + pastorEnTurno.getRiqueza() + ")");

        // Encontrar el NodoPastor correspondiente al pastor actual en turno
        NodoPastor nodoPastorEnTurno = buscarNodoPastor(pastorEnTurno);
        if (nodoPastorEnTurno == null) {
            System.out.println("Error: Pastor en turno no encontrado en la mesa. Saltando turno.");
            avanzarPastorActual(); // Intentar avanzar al siguiente pastor
            return;
        }

        // Regla especial para el pastor más pobre
        // Se obtiene el pastor más pobre de la mesa para comparar
        Pastor pastorMasPobreEnMesa = mesaRedonda.getListaPastores().buscar().getPastor();
        
        // Si el pastor en turno es el más pobre de la mesa
        if (pastorEnTurno.equals(pastorMasPobreEnMesa)) {
            System.out.println(pastorEnTurno.getNombre() + " es el pastor más pobre de la mesa.");
            Pastor pastorMasRicoEnMesa = mesaRedonda.obtenerMasRico();
            
            // Asegurarse de que haya un pastor más rico y que no sea el mismo pastor en turno
            if (pastorMasRicoEnMesa != null && !pastorEnTurno.equals(pastorMasRicoEnMesa)) {
                pastorEnTurno.robarRecursos(pastorMasRicoEnMesa); // El pastor pobre roba al rico
                System.out.println(pastorEnTurno.getNombre() + " ha robado recursos a " + pastorMasRicoEnMesa.getNombre());
                // Reorganizar la mesa después del robo (aunque los recursos cambian, las posiciones no directamente)
                // El requisito indica "después de cada acción", por lo que se llama.
                reorganizarMesaDespuesDeAccion(nodoPastorEnTurno);
            } else {
                System.out.println(pastorEnTurno.getNombre() + " no pudo robar (es el único o no hay otro rico).");
            }
        } else {
            // Acciones normales del turno: Eliminar o Rescatar

            // Decisión: ¿Rescatar o Eliminar?
            // Por ahora, una decisión simple: si la pila de desposeídos no está vacía, 50% de probabilidad de rescatar.
            boolean puedeRescatar = !pilaDesposeidos.estaVacia();
            boolean quiereRescatar = puedeRescatar && aleatorio.nextBoolean(); // 50% de probabilidad de rescatar si es posible

            if (quiereRescatar) {
                // Acción de rescate
                Pastor pastorARescatar = pilaDesposeidos.pop(); // Saca al último pastor de la pila
                if (pastorARescatar != null) {
                    pastorEnTurno.dividirRecursos(pastorARescatar); // El rescatador le da la mitad de sus recursos
                    mesaRedonda.getListaPastores().agregarPastor(pastorARescatar); // Agrega al pastor de vuelta a la mesa
                    System.out.println(pastorEnTurno.getNombre() + " ha rescatado a " + pastorARescatar.getNombre());
                    // Reorganizar la mesa después del rescate
                    reorganizarMesaDespuesDeAccion(nodoPastorEnTurno);
                }
            } else {
                // Acción de eliminación
                // Decidir dirección: true para derecha (siguiente), false para izquierda (anterior)
                boolean sentidoDerecha = aleatorio.nextBoolean();
                int n = 2; // Número fijo de vecinos a considerar (ej. 2)

                // Encontrar al pastor más pobre entre 'n' vecinos
                Pastor pastorAEliminar = mesaRedonda.obtenerMasPobreEntreVecinos(nodoPastorEnTurno, n, sentidoDerecha);

                // Asegurarse de que haya un pastor a eliminar y que no sea el mismo pastor en turno
                if (pastorAEliminar != null && !pastorAEliminar.equals(pastorEnTurno)) {
                    // Encontrar el NodoPastor del pastor a eliminar
                    NodoPastor nodoAEliminar = buscarNodoPastor(pastorAEliminar);
                    if (nodoAEliminar != null) {
                        // Transferir recursos del eliminado al eliminador
                        pastorAEliminar.transferirRecursos(pastorEnTurno, pastorAEliminar.getOvejas(), pastorAEliminar.getRiqueza());
                        
                        // Añadir al pastor eliminado a la pila de desposeídos
                        pilaDesposeidos.push(pastorAEliminar);
                        
                        // Eliminar al pastor de la mesa redonda
                        mesaRedonda.getListaPastores().eliminarPastor(nodoAEliminar);
                        System.out.println(pastorEnTurno.getNombre() + " ha eliminado a " + pastorAEliminar.getNombre());
                        
                        // Reorganizar la mesa después de la eliminación
                        reorganizarMesaDespuesDeAccion(nodoPastorEnTurno);
                    }
                } else {
                    System.out.println(pastorEnTurno.getNombre() + " no encontró un pastor para eliminar o no pudo eliminar.");
                }
            }
        }

        // Avanzar al siguiente pastor para el próximo turno
        avanzarPastorActual();
    }

    /**
     * Verifica si el juego ha terminado.
     * El juego termina cuando solo queda un pastor en la mesa.
     * @return true si el juego ha terminado, false en caso contrario.
     */
    public boolean juegoTerminado() {
        return mesaRedonda.getListaPastores().getSize() <= 1;
    }

    /**
     * Obtiene al pastor ganador del juego.
     * @return El pastor ganador si el juego ha terminado y solo queda uno, de lo contrario null.
     */
    public Pastor obtenerGanador() {
        if (juegoTerminado() && mesaRedonda.getListaPastores().getSize() == 1) {
            return mesaRedonda.getListaPastores().toList().get(0);
        }
        return null;
    }

    /**
     * Método auxiliar para encontrar el NodoPastor correspondiente a un objeto Pastor dado.
     * @param pastorBuscado El objeto Pastor que se desea encontrar.
     * @return El NodoPastor que contiene al pastor buscado, o null si no se encuentra.
     */
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

    /**
     * Método auxiliar para avanzar el pastor actual al siguiente en la lista circular.
     * Si el pastor actual fue eliminado o no se encuentra, se establece la cabeza de la lista como el nuevo pastor actual.
     */
    private void avanzarPastorActual() {
        if (mesaRedonda.getListaPastores().estaVacia()) {
            mesaRedonda.setPastorActual(null);
            return;
        }
        NodoPastor nodoPastorActual = buscarNodoPastor(mesaRedonda.getPastorActual());
        if (nodoPastorActual != null) {
            mesaRedonda.setPastorActual(nodoPastorActual.getSiguiente().getPastor());
        } else {
            // Si el pastor actual fue eliminado o no se encontró, se establece la cabeza de la lista
            mesaRedonda.setPastorActual(mesaRedonda.getListaPastores().getCabeza().getPastor());
        }
    }

    /**
     * Método auxiliar para reorganizar la mesa después de una acción (eliminación, rescate o robo).
     * Este método debe implementar la lógica para asegurar que se cumplan las restricciones de posición:
     * - A la izquierda de un pastor puede haber otro del mismo oficio.
     * - A la derecha, nunca puede haber un pastor con el mismo oficio.
     *
     * NOTA IMPORTANTE: La implementación completa de esta reorganización es compleja
     * y requeriría un algoritmo sofisticado para reordenar los nodos en la ListaCircularDoble
     * de manera que se cumplan las reglas de oficio. Por ahora, es un marcador de posición.
     *
     * @param nodoPastorQueRealizoAccion El nodo del pastor que realizó la última acción.
     */
    private void reorganizarMesaDespuesDeAccion(NodoPastor nodoPastorQueRealizoAccion) {
        // La lógica de reorganización es compleja debido a la regla "mismo oficio a la izquierda, diferente oficio a la derecha".
        // Esto probablemente implicará reinsertar pastores o re-enlazar nodos para satisfacer la regla.
        // Por ahora, un enfoque simplificado: solo asegurar la integridad de la lista circular.
        // El método `reorganizarMesa` en MesaRedonda parece intentar esto, pero su lógica es incompleta/incorrecta.
        // Una reorganización adecuada implicaría iterar a través de la lista y mover/intercambiar nodos
        // hasta que se cumplan las condiciones. Esta es una tarea no trivial.

        System.out.println("Reorganizando la mesa (lógica de oficio pendiente de implementación completa).");
        // Marcador de posición para la lógica real de reorganización basada en las reglas de oficio.
        // Esto podría implicar:
        // 1. Convertir la lista circular a una lista lineal temporal.
        // 2. Ordenar/reorganizar los pastores basándose en las reglas de oficio.
        // 3. Reconstruir la lista circular a partir de los pastores reorganizados.
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