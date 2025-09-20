package co.edu.udistrital.model;

public class NodoPastor {

    //Atributos
    private Pastor pastor;
    private NodoPastor anterior;
    private NodoPastor siguiente;

    //Constructor
    public NodoPastor(Pastor pastor, NodoPastor anterior, NodoPastor siguiente) {
        this.pastor = pastor;
        this.anterior = anterior;
        this.siguiente = siguiente;
    }

    //Getters y Setters
    public Pastor getPastor() {
        return pastor;
    }
    public void setPastor(Pastor pastor) {
        this.pastor = pastor;
    }

    public NodoPastor getAnterior() {
        return anterior;
    }
    public void setAnterior(NodoPastor anterior) {
        this.anterior = anterior;
    }
    
    public NodoPastor getSiguiente() {
        return siguiente;
    }
    public void setSiguiente(NodoPastor siguiente) {
        this.siguiente = siguiente;
    }


}
