package co.edu.udistrital.model;

import java.util.ArrayList;
import java.util.List;

public class MesaRedonda {
    private ListaCircularDoble listaPastores;
    private Pastor pastorActual;

    public MesaRedonda() {
        listaPastores = new ListaCircularDoble();
        pastorActual = null;
    }

    // Obtiene el pastor con mayor riqueza en la mesa
    public Pastor obtenerMasRico() {
        if (listaPastores.estaVacia()) {
            return null;
        }
        NodoPastor cabezaLista = listaPastores.getCabeza();
        if (cabezaLista == null) { // Doble verificación por si la lista se vacía inesperadamente
            return null;
        }
        NodoPastor actualNodo = cabezaLista;
        Pastor pastorMasRico = cabezaLista.getPastor(); // Se inicializa con el primer pastor
        do {
            if (actualNodo.getPastor().esMasRicoQue(pastorMasRico)) {
                pastorMasRico = actualNodo.getPastor();
            }
            actualNodo = actualNodo.getSiguiente();
        } while (actualNodo != cabezaLista); // Recorre toda la lista circular
        return pastorMasRico;
    }

    // Nuevo método para obtener el pastor más pobre entre 'n' vecinos de diferente religión
    public Pastor obtenerMasPobreEntreVecinosDeDiferenteReligion(NodoPastor pastorEnTurnoNodo, int n, boolean sentidoDerecha) {
        if (listaPastores.estaVacia() || n <= 0 || pastorEnTurnoNodo == null) {
            return null;
        }

        Pastor pastorEnTurno = pastorEnTurnoNodo.getPastor();
        Pastor pastorMasPobreDiferenteReligion = null;
        NodoPastor cursor = pastorEnTurnoNodo;
        
        // Lista para almacenar los pastores vecinos de diferente religión encontrados
        List<Pastor> vecinosDiferenteReligion = new ArrayList<>();

        // Recorre 'n' vecinos en la dirección especificada
        for (int i = 0; i < n; i++) {
            cursor = sentidoDerecha ? cursor.getSiguiente() : cursor.getAnterior();
            // Asegurarse de no considerar al propio pastor en turno y que sea de diferente religión
            if (!cursor.getPastor().equals(pastorEnTurno) && !pastorEnTurno.tieneMismaReligion(cursor.getPastor())) {
                vecinosDiferenteReligion.add(cursor.getPastor());
            }
        }

        // Encontrar el más pobre entre los vecinos de diferente religión recolectados
        for (Pastor p : vecinosDiferenteReligion) {
            if (pastorMasPobreDiferenteReligion == null || p.esMasPobreQue(pastorMasPobreDiferenteReligion)) {
                pastorMasPobreDiferenteReligion = p;
            }
        }

        return pastorMasPobreDiferenteReligion;
    }

    // El método obtenerMasPobreEntreVecinos original (no por religión) se mantiene por si acaso,
    // pero no se usa en la lógica actual de eliminación.
    public Pastor obtenerMasPobreEntreVecinos(NodoPastor actual, int n, boolean sentidoDerecha) {
        if (listaPastores.estaVacia() || n <= 0) {
            return null;
        }

        NodoPastor pastorMasPobreNodo = actual; // Inicializa con el pastor actual
        NodoPastor cursor = actual;

        for (int i = 0; i < n; i++) {
            cursor = sentidoDerecha ? cursor.getSiguiente() : cursor.getAnterior();
            if (cursor.getPastor().esMasPobreQue(pastorMasPobreNodo.getPastor())) {
                pastorMasPobreNodo = cursor;
            }
        }

        return pastorMasPobreNodo.getPastor();
    }

    // Método de marcador de posición para la reorganización de la mesa
    public void reorganizarMesa(NodoPastor nuevoCabeza) {
        if (listaPastores.estaVacia() || nuevoCabeza == null) {
            return;
        }

        // La lógica de reorganización es compleja y se mantiene como marcador de posición.
        // Esto debería reordenar los nodos para cumplir las reglas de oficio/religión.
        System.out.println("Reorganizando la mesa (lógica de oficio/religión pendiente de implementación completa).");
    }

    // Getters y Setters
    public ListaCircularDoble getListaPastores() {
        return listaPastores;
    }
    public void setListaPastores(ListaCircularDoble listaPastores) {
        this.listaPastores = listaPastores;
    }

    public Pastor getPastorActual() {
        return this.pastorActual;
    }
    public void setPastorActual(Pastor pastorActual) {
        this.pastorActual = pastorActual;
    }
}