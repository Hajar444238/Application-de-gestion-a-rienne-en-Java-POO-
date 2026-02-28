package enumeration;

public enum TypePassager {
    ADULTE("Adulte"),
    ENFANT("Enfant"),
    BEBE("Bébé"),
    SENIOR("Senior");
    
    private String libelle;
    
    TypePassager(String libelle) {
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
