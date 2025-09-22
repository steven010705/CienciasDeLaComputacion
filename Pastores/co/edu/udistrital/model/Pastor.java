package co.edu.udistrital.model;

public class Pastor {

    //Atributos
    private String nombre;
    private String oficio; // Se mantiene el atributo oficio
    private String religion; // Cambiado de 'ovejas' a 'religion' y tipo String
    private int riqueza;

    //Constructor
    public Pastor(String nombre, String oficio, String religion, int riqueza) { // Constructor actualizado
        this.nombre = nombre;
        this.oficio = oficio;
        this.religion = religion;
        this.riqueza = riqueza;
    }

    //Casos de uso
    public void transferirRecursos(Pastor destinatario, int riquezaTransferida) { // Solo riqueza
        // Caso donde el pastor eliminado debe darle su riqueza al que lo eliminó
        this.riqueza -= riquezaTransferida;
        destinatario.recibirRecursos(riquezaTransferida);
    }

    public void recibirRecursos(int riquezaRecibida) { // Solo riqueza
        // Caso en el que un pastor que eliminó a otro recibe recursos del otro
        this.riqueza += riquezaRecibida;
    }

    public void dividirRecursos(Pastor destinatario){
        // Caso donde el pastor salvador le da la mitad de su riqueza al pastor salvado
        int riquezaParaDar = this.riqueza / 2;
        this.riqueza -= riquezaParaDar;
        destinatario.recibirRecursos(riquezaParaDar); 
    }

    public void robarRecursos(Pastor rico){
        // Caso donde el pastor más pobre le roba la tercera parte de su riqueza al pastor más rico
        int riquezaParaRobar = rico.getRiqueza() / 3;
        rico.setRiqueza(rico.getRiqueza() - riquezaParaRobar);
        this.recibirRecursos(riquezaParaRobar);
    }

    // Comparación para saber quién es más rico o más pobre
    public boolean esMasRicoQue(Pastor otroPastor){
        return this.riqueza > otroPastor.getRiqueza();
    }

    public boolean esMasPobreQue(Pastor otroPastor){
        return this.riqueza < otroPastor.getRiqueza();
    }

    // Nuevo método para verificar si tienen la misma religión
    public boolean tieneMismaReligion(Pastor otroPastor) {
        return this.religion.equals(otroPastor.getReligion());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Pastor pastor = (Pastor) obj;
        return nombre.equals(pastor.nombre);
    }

    @Override
    public int hashCode() {
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

    public String getReligion() { // Getter para religion
        return religion;
    }
    public void setReligion(String religion) { // Setter para religion
        this.religion = religion;
    }

    public int getRiqueza() {
        return riqueza;
    }
    public void setRiqueza(int riqueza) {
        this.riqueza = riqueza;
    }
}