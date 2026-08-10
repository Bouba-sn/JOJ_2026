package service;

import java.util.List;

public interface IStatistiqueService {
    int nombrePays();
    int nombreAthletes();
    int nombreDisciplines();
    int nombreCompetitions();
    int nombreResultats();

    /** Chaque élément : {nomPays, or, argent, bronze, total} */
    List<Object[]> tableauDesMedailles();
}
