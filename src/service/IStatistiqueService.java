package service;
import java.util.List;

public interface IStatistiqueService {
    int nombrePays();
    int nombreAthletes();
    int nombreDisciplines();
    int nombreCompetitions();
    int nombreResultats();
    List<Object[]> tableauDesMedailles();
}
