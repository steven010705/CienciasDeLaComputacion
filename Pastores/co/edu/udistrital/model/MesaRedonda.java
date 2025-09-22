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

    public Pastor obtenerMasRico() {
        if (listaPastores.estaVacia()) {
            return null;
        }
        NodoPastor cabezaLista = listaPastores.getCabeza();
        if (cabezaLista == null) {
            return null;
        }
        NodoPastor actualNodo = cabezaLista;
        Pastor pastorMasRico = cabezaLista.getPastor();
        do {
            if (actualNodo.getPastor().esMasRicoQue(pastorMasRico)) {
                pastorMasRico = actualNodo.getPastor();
            }
            actualNodo = actualNodo.getSiguiente();
        } while (actualNodo != cabezaLista);
        return pastorMasRico;
    }

    // Nuevo método para obtener el pastor más pobre entre vecinos de diferente religión
    public Pastor obtenerMasPobreEntreVecinosDeDiferenteReligion(NodoPastor pastorEnTurnoNodo, int n, boolean sentidoDerecha) {
        if (listaPastores.estaVacia() || n <= 0 || pastorEnTurnoNodo == null) {
            return null;
        }

        Pastor pastorEnTurno = pastorEnTurnoNodo.getPastor();
        Pastor pastorMasPobreDiferenteReligion = null;
        NodoPastor cursor = pastorEnTurnoNodo;
        
        // Lista para almacenar los pastores vecinos de diferente religión
        List<Pastor> vecinosDiferenteReligion = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            cursor = sentidoDerecha ? cursor.getSiguiente() : cursor.getAnterior();
            // Asegurarse de no considerar al propio pastor en turno
            if (!cursor.getPastor().equals(pastorEnTurno) && !pastorEnTurno.tieneMismaReligion(cursor.getPastor())) {
                vecinosDiferenteReligion.add(cursor.getPastor());
            }
        }

        // Encontrar el más pobre entre los vecinos de diferente religión
        for (Pastor p : vecinosDiferenteReligion) {
            if (pastorMasPobreDiferenteReligion == null || p.esMasPobreQue(pastorMasPobreDiferenteReligion)) {
                pastorMasPobreDiferenteReligion = p;
            }
        }

        return pastorMasPobreDiferenteReligion;
    }

    // El método obtenerMasPobreEntreVecinos original ya no se usa para la eliminación,
    // pero se mantiene por si acaso o se puede eliminar si no hay otro uso.
    public Pastor obtenerMasPobreEntreVecinos(NodoPastor actual, int n, boolean sentidoDerecha) {
        if (listaPastores.estaVacia() || n <= 0) {
            return null;
        }

        NodoPastor pastorMasPobre = actual;
        NodoPastor cursor = actual;

        for (int i = 0; i < n; i++) {
            cursor = sentidoDerecha ? cursor.getSiguiente() : cursor.getAnterior();
            if (cursor.getPastor().esMasPobreQue(pastorMasPobre.getPastor())) {
                pastorMasPobre = cursor;
            }
        }

        return pastorMasPobre.getPastor();
    }

    public void reorganizarMesa(NodoPastor nuevoCabeza) {
        if (listaPastores.estaVacia() || nuevoCabeza == null) {
            return;
        }

        // La lógica de reorganización es compleja y se mantiene como marcador de posición.
        // Esto debería reordenar los nodos para cumplir las reglas de oficio/religión.
        System.out.println("Reorganizando la mesa (lógica de oficio/religión pendiente de implementación completa).");
    }

    //Getters y Setters
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