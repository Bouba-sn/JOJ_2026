package service;

import dao.AthleteDAO;
import model.Athlete;

import java.util.List;

public class IAthleteServiceImple implements IAthleteService {

    private final AthleteDAO athleteDAO = new AthleteDAO();

    @Override
    public boolean ajouterAthlete(Athlete a) {
        return athleteDAO.ajouter(a);
    }

    @Override
    public boolean modifierAthlete(Athlete a) {
        return athleteDAO.modifier(a);
    }

    @Override
    public boolean supprimerAthlete(int id) {
        return athleteDAO.supprimer(id);
    }

    @Override
    public List<Athlete> rechercherAthlete(String nom) {
        return athleteDAO.rechercherParNom(nom);
    }

    @Override
    public List<Athlete> listerAthletes() {
        return athleteDAO.listerTous();
    }
}
