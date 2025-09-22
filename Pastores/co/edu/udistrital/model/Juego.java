package co.edu.udistrital.model;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class Juego {

    // Atributos
    private MesaRedonda mesaRedonda;
    private PilaDesposeidos pilaDesposeidos;
    public int numeroTurno;  // Nota: Este es público, pero se accede vía getter en la vista
    private Random aleatorio;

    // Constructor: Acepta el número de pastores como parámetro (resuelve el error "The constructor Juego(int) is undefined")
    public Juego(int nPastores) {
        mesaRedonda = new MesaRedonda();
        pilaDesposeidos = new PilaDesposeidos();
        numeroTurno = 0;  // Se inicializa en 0, el primer turno será el 1
        aleatorio = new Random();
        iniciarJuego(nPastores);  // Llama al método de inicialización con el número de pastores recibido
    }

    // Método: Inicializa el juego con 'n' pastores (sin uso de Scanner, recibe parámetro directamente)
    public void iniciarJuego(int n) {
        if (n < 1) {
            System.out.println("Número de pastores debe ser al menos 1. Se usará 1 por defecto.");
            n = 1;
        }

        numeroTurno = 1;  // El primer turno es el 1
        System.out.println("Inicializando juego con " + n + " pastores...");

        String[] religiones = {"Cristiana", "Musulmana", "Hindú", "Budista", "Judía", "Sintoísta"};

        // Creación dinámica de n pastores con atributos aleatorios
        for (int i = 1; i <= n; i++) {
            String nombre;
            if (i <= 26) {
                nombre = "Pastor " + (char)('A' + i - 1);  // Nombres como Pastor A, B, C... hasta Z
            } else {
                nombre = "Pastor " + i;  // Para más de 26: Pastor 27, etc.
            }

            String oficio = "Oficio " + ((i - 1) % 3 + 1);  // Cicla entre Oficio 1, 2, 3
            String religion = religiones[aleatorio.nextInt(religiones.length)];  // Asigna una religión aleatoria
            int riqueza = aleatorio.nextInt(801) + 200;  // Riqueza aleatoria entre 200 y 1000

            Pastor p = new Pastor(nombre, oficio, religion, riqueza);
            mesaRedonda.getListaPastores().agregarPastor(p);  // Agrega a la lista circular de la mesa
            System.out.println("Creado: " + nombre + " (Oficio: " + oficio + ", Religión: " + religion + ", Riqueza: " + riqueza + ")");
        }

        // Configuración inicial: El juego comienza con el pastor más rico como el actual
        if (!mesaRedonda.getListaPastores().estaVacia()) {
            mesaRedonda.setPastorActual(mesaRedonda.obtenerMasRico());
            System.out.println("El juego comienza con: " + mesaRedonda.getPastorActual().getNombre());
        }
    }

    // Método para ejecutar un turno completo (lógica automática de IA para el juego; no usada en GUI, pero mantenida para simulación)
    public void ejecutarTurno() {
        if (juegoTerminado()) {
            return;  // Si el juego ya terminó, no hace nada
        }

        numeroTurno++;  // Incrementa el número de turno
        Pastor pastorEnTurno = mesaRedonda.getPastorActual();
        System.out.println("\n--- Turno " + numeroTurno + " ---");
        System.out.println("Es el turno de: " + pastorEnTurno.getNombre() + " (Religión: " + pastorEnTurno.getReligion() + ", Riqueza: " + pastorEnTurno.getRiqueza() + ")");

        NodoPastor nodoPastorEnTurno = buscarNodoPastor(pastorEnTurno);
        if (nodoPastorEnTurno == null) {
            System.out.println("Error: Pastor en turno no encontrado en la mesa. Saltando turno.");
            avanzarPastorActual();
            return;
        }

        // Verifica si el pastor en turno es el más pobre de la mesa (puede robar)
        Pastor pastorMasPobreEnMesa = null;
        if (!mesaRedonda.getListaPastores().estaVacia()) {
            pastorMasPobreEnMesa = mesaRedonda.getListaPastores().buscar().getPastor();  // Busca el más pobre
        }
        
        if (pastorMasPobreEnMesa != null && pastorEnTurno.equals(pastorMasPobreEnMesa)) {
            System.out.println(pastorEnTurno.getNombre() + " es el pastor más pobre de la mesa.");
            Pastor pastorMasRicoEnMesa = mesaRedonda.obtenerMasRico();  // Obtiene el más rico para robarle
            
            if (pastorMasRicoEnMesa != null && !pastorEnTurno.equals(pastorMasRicoEnMesa)) {
                pastorEnTurno.robarRecursos(pastorMasRicoEnMesa);  // El más pobre roba al más rico
                System.out.println(pastorEnTurno.getNombre() + " ha robado recursos a " + pastorMasRicoEnMesa.getNombre());
                reorganizarMesaDespuesDeAccion(nodoPastorEnTurno);  // Reorganiza la mesa después de la acción
            } else {
                System.out.println(pastorEnTurno.getNombre() + " no pudo robar (es el único o no hay otro rico).");
            }
        } else {
            // Lógica alternativa: Intenta rescatar (solo de misma religión) o eliminar (de diferente religión)
            boolean puedeRescatar = !pilaDesposeidos.estaVacia();  // Verifica si hay desposeídos para rescatar
            boolean quiereRescatar = puedeRescatar && aleatorio.nextBoolean();  // Decisión aleatoria

            if (quiereRescatar) {
                // Intenta rescatar un pastor de la misma religión de la pila
                Pastor pastorARescatar = pilaDesposeidos.popPastorDeMismaReligion(pastorEnTurno.getReligion());
                if (pastorARescatar != null) {
                    pastorEnTurno.dividirRecursos(pastorARescatar);  // Divide la riqueza con el rescatado
                    mesaRedonda.getListaPastores().agregarPastor(pastorARescatar);  // Lo agrega de vuelta a la mesa
                    System.out.println(pastorEnTurno.getNombre() + " ha rescatado a " + pastorARescatar.getNombre());
                    reorganizarMesaDespuesDeAccion(nodoPastorEnTurno);  // Reorganiza la mesa
                } else {
                    System.out.println(pastorEnTurno.getNombre() + " quiso rescatar, pero no encontró un pastor de su misma religión en la pila.");
                }
            } else {
                // Acción de eliminación: Busca el más pobre de diferente religión entre vecinos
                boolean sentidoDerecha = aleatorio.nextBoolean();  // Dirección aleatoria (derecha o izquierda)
                int vecinos = 2;  // Número de vecinos a considerar

                Pastor pastorAEliminar = mesaRedonda.obtenerMasPobreEntreVecinosDeDiferenteReligion(nodoPastorEnTurno, vecinos, sentidoDerecha);

                if (pastorAEliminar != null && !pastorAEliminar.equals(pastorEnTurno)) {
                    NodoPastor nodoAEliminar = buscarNodoPastor(pastorAEliminar);
                    if (nodoAEliminar != null) {
                        pastorAEliminar.transferirRecursos(pastorEnTurno, pastorAEliminar.getRiqueza());  // Transfiere toda la riqueza
                        pilaDesposeidos.push(pastorAEliminar);  // Mueve a la pila de desposeídos
                        mesaRedonda.getListaPastores().eliminarPastor(nodoAEliminar);  // Elimina de la mesa
                        System.out.println(pastorEnTurno.getNombre() + " ha eliminado a " + pastorAEliminar.getNombre() + " por ser de diferente religión.");
                        reorganizarMesaDespuesDeAccion(nodoPastorEnTurno);  // Reorganiza la mesa
                    }
                } else {
                    System.out.println(pastorEnTurno.getNombre() + " no encontró un pastor de diferente religión para eliminar o no pudo eliminar.");
                }
            }
        }

        avanzarPastorActual();  // Avanza al siguiente pastor para el próximo turno
    }

    // Verifica si el juego ha terminado (queda 1 pastor o menos en la mesa)
    public boolean juegoTerminado() {
        return mesaRedonda.getListaPastores().getSize() <= 1;
    }

    // Obtiene el pastor ganador si el juego ha terminado (el último en la mesa)
    public Pastor obtenerGanador() {
        if (juegoTerminado() && mesaRedonda.getListaPastores().getSize() == 1) {
            return mesaRedonda.getListaPastores().toList().get(0);  // Retorna el único pastor restante
        }
        return null;  // No hay ganador si hay 0 o más de 1
    }

    // Método auxiliar privado: Busca un NodoPastor dado un Pastor en la lista circular
    private NodoPastor buscarNodoPastor(Pastor pastorBuscado) {
        if (mesaRedonda.getListaPastores().estaVacia()) {
            return null;
        }
        NodoPastor actual = mesaRedonda.getListaPastores().getCabeza();
        if (actual == null) return null;  // Manejo de caso de lista vacía después de obtener cabeza
        do {
            if (actual.getPastor().equals(pastorBuscado)) {  // Compara por igualdad (basada en nombre)
                return actual;
            }
            actual = actual.getSiguiente();
        } while (actual != mesaRedonda.getListaPastores().getCabeza());  // Recorre la lista circular completa
        return null;  // No encontrado
    }

    // Método auxiliar privado: Avanza al siguiente pastor en el turno (actualiza pastorActual)
    private void avanzarPastorActual() {
        // Este método es llamado por ejecutarTurno, pero la lógica de avance de turno
        // para la GUI se maneja en ControlJuego.avanzarPastorActual()
        if (mesaRedonda.getListaPastores().estaVacia()) {
            mesaRedonda.setPastorActual(null);  // Si no hay pastores, no hay pastor actual
            return;
        }
        NodoPastor nodoPastorActual = buscarNodoPastor(mesaRedonda.getPastorActual());
        if (nodoPastorActual != null) {
            mesaRedonda.setPastorActual(nodoPastorActual.getSiguiente().getPastor());  // Avanza al siguiente en la lista circular
        } else {
            // Si el pastor actual no se encontró (ej. fue eliminado), se toma la cabeza de la lista
            mesaRedonda.setPastorActual(mesaRedonda.getListaPastores().getCabeza().getPastor());
        }
    }

    // Método auxiliar privado: Reorganiza la mesa después de una acción (marcador de posición para lógica compleja)
    private void reorganizarMesaDespuesDeAccion(NodoPastor nodoPastorQueRealizoAccion) {
        // Lógica de reorganización de la mesa según reglas de oficio/religión
        // Esta parte es compleja y se mantiene como un marcador de posición.
        // Implicaría reordenar los nodos en la ListaCircularDoble para cumplir reglas específicas (ej. agrupar por religión/oficio).
        // Por ahora, solo imprime un mensaje; se puede expandir en futuras iteraciones.
        System.out.println("Reorganizando la mesa (lógica de oficio/religión pendiente de implementación completa).");
    }

    // Getters (accesores para el controlador y la vista)
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