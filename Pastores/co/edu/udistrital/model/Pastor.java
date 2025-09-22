package co.edu.udistrital.model;

public class Pastor {

    //Atributos
    private String nombre;
    private String oficio;
    private String religion;
    private int riqueza;

    //Constructor
    public Pastor(String nombre, String oficio, String religion, int riqueza) {
        this.nombre = nombre;
        this.oficio = oficio;
        this.religion = religion;
        this.riqueza = riqueza;
    }

    //Casos de uso

    // Transfiere una cantidad de riqueza a otro pastor (usado en eliminación)
    public void transferirRecursos(Pastor destinatario, int riquezaTransferida) {
        this.riqueza -= riquezaTransferida;
        destinatario.recibirRecursos(riquezaTransferida);
    }

    // Recibe una cantidad de riqueza
    public void recibirRecursos(int riquezaRecibida) {
        this.riqueza += riquezaRecibida;
    }

    // Divide la riqueza con otro pastor (usado en rescate)
    public void dividirRecursos(Pastor destinatario){
        int riquezaParaDar = this.riqueza / 2; // Da la mitad de su riqueza
        this.riqueza -= riquezaParaDar;
        destinatario.recibirRecursos(riquezaParaDar);
    }

    // Roba una parte de la riqueza de otro pastor (usado por el más pobre)
    public void robarRecursos(Pastor rico){
        int riquezaParaRobar = rico.getRiqueza() / 3; // Roba la tercera parte
        rico.setRiqueza(rico.getRiqueza() - riquezaParaRobar);
        this.recibirRecursos(riquezaParaRobar);
    }

    // Compara si este pastor es más rico que otro
    public boolean esMasRicoQue(Pastor otroPastor){
        return this.riqueza > otroPastor.getRiqueza();
    }

    // Compara si este pastor es más pobre que otro
    public boolean esMasPobreQue(Pastor otroPastor){
        return this.riqueza < otroPastor.getRiqueza();
    }

    // Verifica si este pastor tiene la misma religión que otro
    public boolean tieneMismaReligion(Pastor otroPastor) {
        return this.religion.equals(otroPastor.getReligion());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Pastor pastor = (Pastor) obj;
        return nombre.equals(pastor.nombre); // La igualdad se basa en el nombre
    }

    @Override
    public int hashCode() {
        return nombre.hashCode();
    }

    // Getters y Setters
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

    public String getReligion() {
        return religion;
    }
    public void setReligion(String religion) {
        this.religion = religion;
    }

    public int getRiqueza() {
        return riqueza;
    }
    public void setRiqueza(int riqueza) {
        this.riqueza = riqueza;
    }
}