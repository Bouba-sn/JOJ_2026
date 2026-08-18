package model;
import java.time.LocalDate;
public class Athlete {
    private int idAthlete;
    private String nom;
    private String prenom;
    private String sexe; // "M" ou "F"
    private LocalDate dateNaissance;
    private int idPays;
    private int idDiscipline;
    private String nomPays;
    private String nomDiscipline;

    public Athlete() {}

    public Athlete(int idAthlete, String nom, String prenom, String sexe,
                   LocalDate dateNaissance, int idPays, int idDiscipline) {
        this.idAthlete = idAthlete;
        this.nom = nom;
        this.prenom = prenom;
        this.sexe = sexe;
        this.dateNaissance = dateNaissance;
        this.idPays = idPays;
        this.idDiscipline = idDiscipline;
    }
    public Athlete(String nom, String prenom, String sexe,
                   LocalDate dateNaissance, int idPays, int idDiscipline) {
        this.nom = nom;
        this.prenom = prenom;
        this.sexe = sexe;
        this.dateNaissance = dateNaissance;
        this.idPays = idPays;
        this.idDiscipline = idDiscipline;
    }
    public int getIdAthlete() { return idAthlete; }
    public void setIdAthlete(int idAthlete) { this.idAthlete = idAthlete; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getSexe() { return sexe; }
    public void setSexe(String sexe) { this.sexe = sexe; }
    public LocalDate getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }
    public int getIdPays() { return idPays; }
    public void setIdPays(int idPays) { this.idPays = idPays; }
    public int getIdDiscipline() { return idDiscipline; }
    public void setIdDiscipline(int idDiscipline) { this.idDiscipline = idDiscipline; }
    public String getNomPays() { return nomPays; }
    public void setNomPays(String nomPays) { this.nomPays = nomPays; }
    public String getNomDiscipline() { return nomDiscipline; }
    public void setNomDiscipline(String nomDiscipline) { this.nomDiscipline = nomDiscipline; }

    @Override
    public String toString() {
        return "[" + idAthlete + "] " + prenom + " " + nom + " (" + sexe + ") - Né(e) le " + dateNaissance
                + " | Pays: " + (nomPays != null ? nomPays : idPays)
                + " | Discipline: " + (nomDiscipline != null ? nomDiscipline : idDiscipline);
    }
}
