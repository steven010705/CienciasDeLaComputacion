package co.edu.udistrital.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListaCircularDoble {
    private NodoPastor cabeza;
    private int size;

    public List<Pastor> toList() {
        List<Pastor> lista = new ArrayList<>();
        if (estaVacia()) return lista;

        NodoPastor actual = cabeza;
        NodoPastor inicio = actual;
        do {
            lista.add(actual.getPastor());
            actual = actual.getSiguiente();
        } while (actual != inicio);

        return lista;

    }

    public void agregarPastor(Pastor pastor) {
        NodoPastor nuevoNodo = new NodoPastor(pastor, null, null);
        if (cabeza == null) {
            cabeza = nuevoNodo;
            cabeza.setSiguiente(cabeza);
            cabeza.setAnterior(cabeza);
        } else {
            NodoPastor cola = cabeza.getAnterior();
            cola.setSiguiente(nuevoNodo);
            nuevoNodo.setAnterior(cola);
            nuevoNodo.setSiguiente(cabeza);
            cabeza.setAnterior(nuevoNodo);
        }
        size++;
    }

    public void eliminarPastor(NodoPastor nodo) {
        if (nodo == null || cabeza == null) return;

        if (nodo == cabeza && size == 1) {
            cabeza = null;
        } else {
            NodoPastor anterior = nodo.getAnterior();
            NodoPastor siguiente = nodo.getSiguiente();
            anterior.setSiguiente(siguiente);
            siguiente.setAnterior(anterior);
            if (nodo == cabeza) {
                cabeza = siguiente;
            }
        }
        size--;
    }

    public NodoPastor buscar(){
        if (cabeza == null) return null;

        NodoPastor actual = cabeza;
        NodoPastor pastorMasPobre = cabeza;

        do {
            if (actual.getPastor().esMasPobreQue(pastorMasPobre.getPastor())) {
                pastorMasPobre = actual;
            }
            actual = actual.getSiguiente();
        } while (actual != cabeza);

        return pastorMasPobre;
    }

    public boolean estaVacia() {
        return size == 0;
    }

    public Iterator<Pastor> iterator() {
        return new Iterator<Pastor>() {
            private NodoPastor actual = cabeza;
            private int cont = 0;

            @Override
            public boolean hasNext() {
                return cont < size;
            }

            @Override
            public Pastor next() {
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