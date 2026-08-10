package model;

public class Resultat {
    private int idResultat;
    private int idAthlete;
    private int idCompetition;
    private double score;
    private int rang;

    // Pour affichage
    private String nomAthlete;
    private String nomCompetition;

    public Resultat() {}

    public Resultat(int idResultat, int idAthlete, int idCompetition, double score, int rang) {
        this.idResultat = idResultat;
        this.idAthlete = idAthlete;
        this.idCompetition = idCompetition;
        this.score = score;
        this.rang = rang;
    }

    public Resultat(int idAthlete, int idCompetition, double score, int rang) {
        this.idAthlete = idAthlete;
        this.idCompetition = idCompetition;
        this.score = score;
        this.rang = rang;
    }

    public int getIdResultat() { return idResultat; }
    public void setIdResultat(int idResultat) { this.idResultat = idResultat; }

    public int getIdAthlete() { return idAthlete; }
    public void setIdAthlete(int idAthlete) { this.idAthlete = idAthlete; }

    public int getIdCompetition() { return idCompetition; }
    public void setIdCompetition(int idCompetition) { this.idCompetition = idCompetition; }

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }

    public int getRang() { return rang; }
    public void setRang(int rang) { this.rang = rang; }

    public String getNomAthlete() { return nomAthlete; }
    public void setNomAthlete(String nomAthlete) { this.nomAthlete = nomAthlete; }

    public String getNomCompetition() { return nomCompetition; }
    public void setNomCompetition(String nomCompetition) { this.nomCompetition = nomCompetition; }

    /** Renvoie le libellé de la médaille selon le rang (1=Or, 2=Argent, 3=Bronze). */
    public String getMedaille() {
        switch (rang) {
            case 1: return "Or";
            case 2: return "Argent";
            case 3: return "Bronze";
            default: return "-";
        }
    }

    @Override
    public String toString() {
        return "[" + idResultat + "] Athlète: " + (nomAthlete != null ? nomAthlete : idAthlete)
                + " | Compétition: " + (nomCompetition != null ? nomCompetition : idCompetition)
                + " | Score: " + score + " | Rang: " + rang + " (" + getMedaille() + ")";
    }
}
