package model;
import java.time.LocalDate;
public class Competition {
    private int idCompetition;
    private String nomCompetition;
    private LocalDate dateCompetition;
    private String lieu;
    private int idDiscipline;
    private String nomDiscipline;

    public Competition() {}

    public Competition(int idCompetition, String nomCompetition, LocalDate dateCompetition,
                       String lieu, int idDiscipline) {
        this.idCompetition = idCompetition;
        this.nomCompetition = nomCompetition;
        this.dateCompetition = dateCompetition;
        this.lieu = lieu;
        this.idDiscipline = idDiscipline;
    }
    public Competition(String nomCompetition, LocalDate dateCompetition, String lieu, int idDiscipline) {
        this.nomCompetition = nomCompetition;
        this.dateCompetition = dateCompetition;
        this.lieu = lieu;
        this.idDiscipline = idDiscipline;
    }
    public int getIdCompetition() { return idCompetition; }
    public void setIdCompetition(int idCompetition) { this.idCompetition = idCompetition; }
    public String getNomCompetition() { return nomCompetition; }
    public void setNomCompetition(String nomCompetition) { this.nomCompetition = nomCompetition; }
    public LocalDate getDateCompetition() { return dateCompetition; }
    public void setDateCompetition(LocalDate dateCompetition) { this.dateCompetition = dateCompetition; }
    public String getLieu() { return lieu; }
    public void setLieu(String lieu) { this.lieu = lieu; }
    public int getIdDiscipline() { return idDiscipline; }
    public void setIdDiscipline(int idDiscipline) { this.idDiscipline = idDiscipline; }
    public String getNomDiscipline() { return nomDiscipline; }
    public void setNomDiscipline(String nomDiscipline) { this.nomDiscipline = nomDiscipline; }

    @Override
    public String toString() {
        return "[" + idCompetition + "] " + nomCompetition + " - " + dateCompetition
                + " @ " + lieu + " | Discipline: " + (nomDiscipline != null ? nomDiscipline : idDiscipline);
    }
}
