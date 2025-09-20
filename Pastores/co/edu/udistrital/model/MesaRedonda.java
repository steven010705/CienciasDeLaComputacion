package co.edu.udistrital.model;

public class MesaRedonda {
    private ListaCircularDoble listaPastores;
    private Pastor pastorActual;

    public MesaRedonda() {
        listaPastores = new ListaCircularDoble();
        pastorActual = null;
    }

    /*public void agregarPastor(Pastor pastor) {
        listaPastores.agregarPastor(pastor);
    }

    public void eliminarPastor(NodoPastor nodo) {
        listaPastores.eliminarPastor(nodo);
    }*/

    public Pastor obtenerMasRico() {
        if (listaPastores.estaVacia()) {
            return null;
        }
        NodoPastor cabezaLista = listaPastores.getCabeza(); // Obtiene la cabeza actual de la lista 
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
        } while (actualNodo != cabezaLista); // Loop until we return to the head
        return pastorMasRico;
    }

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

        NodoPastor cola = listaPastores.buscar().getAnterior();
        cola.setSiguiente(nuevoCabeza);
        nuevoCabeza.setAnterior(cola);
        listaPastores.buscar().setAnterior(nuevoCabeza.getAnterior());
        nuevoCabeza.getAnterior().setSiguiente(listaPastores.buscar());
        listaPastores.buscar().setAnterior(cola);
        nuevoCabeza.setAnterior(cola);
    }

    //Getters y Setters
    public ListaCircularDoble getListaPastores() {
        return listaPastores;
    }
    public void setListaPastores(ListaCircularDoble listaPastores) {
        this.listaPastores = listaPastores;
    }

    public Pastor getPastorActual() {
        return this.pastorActual; // Asegúrate de tener un atributo pastorActual
    }
    public void setPastorActual(Pastor pastorActual) {
        this.pastorActual = pastorActual;
    }

}
