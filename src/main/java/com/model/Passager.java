package model;

import enumeration.Nationalite;
import enumeration.TypePassager;
import enumeration.Genre;
import java.util.Date;

public class Passager {
    
    // Attributs
    private Integer idPassager;
    private String nom;
    private String prenom;
    private Date dateNaissance;
    private String numPasseport;
    private Nationalite nationalite;
    private TypePassager typePassager;
    private Genre genre;
    private String email;
    private String telephone;
    
    // Constructeur par défaut
    public Passager() {
    }
    
    // Constructeur avec tous les paramètres
    public Passager(Integer idPassager, String nom, String prenom, Date dateNaissance,
                   String numPasseport, Nationalite nationalite, TypePassager typePassager,
                   Genre genre, String email, String telephone) {
        this.idPassager = idPassager;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.numPasseport = numPasseport;
        this.nationalite = nationalite;
        this.typePassager = typePassager;
        this.genre = genre;
        this.email = email;
        this.telephone = telephone;
    }
    
    // Constructeur sans ID (pour l'ajout)
    public Passager(String nom, String prenom, Date dateNaissance,
                   String numPasseport, Nationalite nationalite, TypePassager typePassager,
                   Genre genre, String email, String telephone) {
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.numPasseport = numPasseport;
        this.nationalite = nationalite;
        this.typePassager = typePassager;
        this.genre = genre;
        this.email = email;
        this.telephone = telephone;
    }
    
    // Getters
    public Integer getIdPassager() {
        return idPassager;
    }
    
    public String getNom() {
        return nom;
    }
    
    public String getPrenom() {
        return prenom;
    }
    
    public Date getDateNaissance() {
        return dateNaissance;
    }
    
    public String getNumPasseport() {
        return numPasseport;
    }
    
    public Nationalite getNationalite() {
        return nationalite;
    }
    
    public TypePassager getTypePassager() {
        return typePassager;
    }
    
    public Genre getGenre() {
        return genre;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getTelephone() {
        return telephone;
    }
    
    // Setters
    public void setIdPassager(Integer idPassager) {
        this.idPassager = idPassager;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    
    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }
    
    public void setNumPasseport(String numPasseport) {
        this.numPasseport = numPasseport;
    }
    
    public void setNationalite(Nationalite nationalite) {
        this.nationalite = nationalite;
    }
    
    public void setTypePassager(TypePassager typePassager) {
        this.typePassager = typePassager;
    }
    
    public void setGenre(Genre genre) {
        this.genre = genre;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    
    // toString
    @Override
    public String toString() {
        return "Passager{" +
                "idPassager=" + idPassager +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", dateNaissance=" + dateNaissance +
                ", numPasseport='" + numPasseport + '\'' +
                ", nationalite=" + nationalite +
                ", typePassager=" + typePassager +
                ", genre=" + genre +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                '}';
    }
    
    // equals et hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Passager passager = (Passager) o;
        return idPassager != null && idPassager.equals(passager.idPassager);
    }
    
    @Override
    public int hashCode() {
        return idPassager != null ? idPassager.hashCode() : 0;
    }
}
