package co.edu.udistrital.model;

import java.util.Iterator;

public class IteratorPastores implements Iterator<Pastor> {
    private NodoPastor actual;
    private NodoPastor cabeza;
    private boolean recorridoCompleto;

    public IteratorPastores(NodoPastor cabeza) {
        this.cabeza = cabeza;
        this.actual = cabeza;
        this.recorridoCompleto = false;
    }

    public boolean hasNext() {
        return actual != null && (!recorridoCompleto || actual != cabeza);
    }

    public Pastor next() {
        if (!hasNext()) {
            return null;
        }
        Pastor pastor = actual.getPastor();
        actual = actual.getSiguiente();
        if (actual == cabeza) {
            recorridoCompleto = true;
        }
        return pastor;
    }
}