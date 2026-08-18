package service;
import model.Athlete;
import java.util.List;

public interface IAthleteService {
    boolean ajouterAthlete(Athlete a);
    boolean modifierAthlete(Athlete a);
    boolean supprimerAthlete(int id);
    List<Athlete> rechercherAthlete(String nom);
    List<Athlete> listerAthletes();
}
