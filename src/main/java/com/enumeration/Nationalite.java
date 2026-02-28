package enumeration;

public enum Nationalite {
    MAROCAINE("Marocaine"),
    FRANCAISE("Française"),
    ESPAGNOLE("Espagnole"),
    AMERICAINE("Américaine"),
    BRITANNIQUE("Britannique"),
    ALLEMANDE("Allemande"),
    ITALIENNE("Italienne"),
    CANADIENNE("Canadienne"),
    CHINOISE("Chinoise"),
    JAPONAISE("Japonaise"),
    AUTRE("Autre");
    
    private String libelle;
    
    Nationalite(String libelle) {
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
