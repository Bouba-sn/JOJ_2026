package service;

import dao.AthleteDAO;
import dao.CompetitionDAO;
import dao.DisciplineDAO;
import dao.PaysDAO;
import dao.ResultatDAO;
import java.util.List;

public class IStatistiqueServiceImple implements IStatistiqueService {
    private final PaysDAO paysDAO = new PaysDAO();
    private final AthleteDAO athleteDAO = new AthleteDAO();
    private final DisciplineDAO disciplineDAO = new DisciplineDAO();
    private final CompetitionDAO competitionDAO = new CompetitionDAO();
    private final ResultatDAO resultatDAO = new ResultatDAO();

    @Override
    public int nombrePays() {
        return paysDAO.compter();
    }
    @Override
    public int nombreAthletes() {
        return athleteDAO.compter();
    }
    @Override
    public int nombreDisciplines() {
        return disciplineDAO.compter();
    }
    @Override
    public int nombreCompetitions() {
        return competitionDAO.compter();
    }
    @Override
    public int nombreResultats() {
        return resultatDAO.compter();
    }
    @Override
    public List<Object[]> tableauDesMedailles() {
        return resultatDAO.tableauDesMedailles();
    }
}
