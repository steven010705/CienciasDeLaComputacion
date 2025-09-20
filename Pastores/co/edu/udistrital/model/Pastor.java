package co.edu.udistrital.model;

public class Pastor {

    //Atributos
    private String nombre;
    private String oficio;
    private int ovejas;
    private int riqueza;

    //Constructor
    public Pastor(String nombre, String oficio, int ovejas, int riqueza) {
        this.nombre = nombre;
        this.oficio = oficio;
        this.ovejas = ovejas;
        this.riqueza = riqueza;
    }

    //Casos de uso
    public void transferirRecursos(Pastor destinatario, int ovejas, int riqueza) {
        //Caso donde el pastor eliminado (que tiene menos ovejas) debe darle sus ovejas y riquezas al que lo eliminó
        this.ovejas -= ovejas;
        this.riqueza -= riqueza;
        destinatario.recibirRecursos(ovejas, riqueza);
    }

    public void recibirRecursos(int ovejas, int riqueza) {
        //Caso en el que un pastor que eliminó a otro recibe recursos del otro
        this.ovejas += ovejas;
        this.riqueza += riqueza;
    }

    public void dividirRecursos(Pastor destinatario){
        //Caso donde el pastor salvador le da la mitad de sus recursos al pastor salvado
        int ovejasParaDar = this.ovejas / 2;
        int riquezaParaDar = this.riqueza / 2;
        this.ovejas -= ovejasParaDar;
        this.riqueza -= riquezaParaDar;
        destinatario.recibirRecursos(ovejasParaDar, riquezaParaDar); 
    }

    public void robarRecursos(Pastor rico){
        //Caso donde el pastor más pobre le roba la tercera parte de sus recursos al pastor más rico
        int ovejasParaRobar = rico.getOvejas() / 3;
        int riquezaParaRobar = rico.getRiqueza() / 3;
        rico.setOvejas(rico.getOvejas() - ovejasParaRobar);
        rico.setRiqueza(rico.getRiqueza() - riquezaParaRobar);
        this.recibirRecursos(ovejasParaRobar, riquezaParaRobar);
    }

    //Comparación para saber quién es más rico o más pobre
    public boolean esMasRicoQue(Pastor otroPastor){
        return this.riqueza > otroPastor.getRiqueza();
    }

    public boolean esMasPobreQue(Pastor otroPastor){
        return this.riqueza < otroPastor.getRiqueza();
    }

    
@Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Pastor pastor = (Pastor) obj;
        // Asumiendo que el nombre es un identificador único para un pastor.
        // Si hay un ID único, sería mejor usarlo.
        return nombre.equals(pastor.nombre);
    }

    @Override
    public int hashCode() {
        // Genera un código hash basado en el nombre.
        return nombre.hashCode();
    }

    //Getters y Setters
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getOficio() {
        return oficio;
    }
    public void setOficio(String oficio) {
        this.oficio = oficio;
    }

    public int getOvejas() {
        return ovejas;
    }
    public void setOvejas(int ovejas) {
        this.ovejas = ovejas;
    }

    public int getRiqueza() {
        return riqueza;
    }
    public void setRiqueza(int riqueza) {
        this.riqueza = riqueza;
    }


}