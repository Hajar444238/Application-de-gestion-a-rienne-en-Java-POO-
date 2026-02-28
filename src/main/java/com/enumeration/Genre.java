package enumeration;

public enum Genre {
    MASCULIN("Masculin"),
    FEMININ("Féminin");
    
    private String libelle;
    
    Genre(String libelle) {
        this.libelle = libelle;
    }
    
    public String getLibelle() {
        return libelle;
    }
    
    @Override
    public String toString() {
        return libelle;
    }
}
