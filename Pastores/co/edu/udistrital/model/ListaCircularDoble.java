package co.edu.udistrital.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListaCircularDoble {
    private NodoPastor cabeza;
    private int size;

    // Convierte la lista circular a una lista de tipo List<Pastor>
    public List<Pastor> toList() {
        List<Pastor> lista = new ArrayList<>();
        if (estaVacia()) return lista;

        NodoPastor actual = cabeza;
        NodoPastor inicio = actual;
        do {
            lista.add(actual.getPastor());
            actual = actual.getSiguiente();
        } while (actual != inicio); // Recorre hasta volver al inicio

        return lista;
    }

    // Agrega un nuevo pastor a la lista circular
    public void agregarPastor(Pastor pastor) {
        NodoPastor nuevoNodo = new NodoPastor(pastor, null, null);
        if (cabeza == null) {
            cabeza = nuevoNodo;
            cabeza.setSiguiente(cabeza); // Apunta a sí mismo
            cabeza.setAnterior(cabeza); // Apunta a sí mismo
        } else {
            NodoPastor cola = cabeza.getAnterior(); // Obtiene el último nodo
            cola.setSiguiente(nuevoNodo); // El último nodo apunta al nuevo
            nuevoNodo.setAnterior(cola); // El nuevo nodo apunta al anterior último
            nuevoNodo.setSiguiente(cabeza); // El nuevo nodo apunta a la cabeza
            cabeza.setAnterior(nuevoNodo); // La cabeza apunta al nuevo nodo como su anterior
        }
        size++;
    }

    // Elimina un nodo pastor específico de la lista
    public void eliminarPastor(NodoPastor nodo) {
        if (nodo == null || cabeza == null) return; // No se puede eliminar si el nodo es nulo o la lista está vacía

        if (nodo == cabeza && size == 1) {
            cabeza = null; // Si es el único nodo, la lista queda vacía
        } else {
            NodoPastor anterior = nodo.getAnterior();
            NodoPastor siguiente = nodo.getSiguiente();
            anterior.setSiguiente(siguiente); // El anterior del nodo eliminado apunta al siguiente
            siguiente.setAnterior(anterior); // El siguiente del nodo eliminado apunta al anterior
            if (nodo == cabeza) {
                cabeza = siguiente; // Si el nodo eliminado era la cabeza, el siguiente se convierte en la nueva cabeza
            }
        }
        size--;
    }

    // Busca el pastor con menor riqueza en la lista
    public NodoPastor buscar(){
        if (cabeza == null) return null;

        NodoPastor actual = cabeza;
        NodoPastor pastorMasPobre = cabeza; // Se inicializa con la cabeza

        do {
            if (actual.getPastor().esMasPobreQue(pastorMasPobre.getPastor())) {
                pastorMasPobre = actual;
            }
            actual = actual.getSiguiente();
        } while (actual != cabeza); // Recorre toda la lista circular

        return pastorMasPobre;
    }

    // Verifica si la lista está vacía
    public boolean estaVacia() {
        return size == 0;
    }

    // Implementación de un iterador para la lista circular
    public Iterator<Pastor> iterator() {
        return new Iterator<Pastor>() {
            private NodoPastor actual = cabeza;
            private int cont = 0; // Contador para saber cuántos elementos se han recorrido

            @Override
            public boolean hasNext() {
                return cont < size; // Hay más elementos si el contador es menor que el tamaño
            }

            @Override
            public Pastor next() {
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException();
                }
                Pastor pastor = actual.getPastor();
                actual = actual.getSiguiente();
                cont++;
                return pastor;
            }
        };
    }

    // Getters y Setters
    public NodoPastor getCabeza() {
        return cabeza;
    }
    public void setCabeza(NodoPastor cabeza) {
        this.cabeza = cabeza;
    }

    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }
}