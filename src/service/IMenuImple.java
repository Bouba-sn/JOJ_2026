package service;

import dao.AthleteDAO;
import dao.CompetitionDAO;
import dao.DisciplineDAO;
import dao.PaysDAO;
import dao.ResultatDAO;
import dao.UtilisateurDAO;
import model.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class IMenuImple implements IMenu {
    private final Scanner sc = new Scanner(System.in);
    private final DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final IAuthService authService = new IAuthServiceImple();
    private final IAthleteService athleteService = new IAthleteServiceImple();
    private final IStatistiqueService statistiqueService = new IStatistiqueServiceImple();
    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
    private final PaysDAO paysDAO = new PaysDAO();
    private final DisciplineDAO disciplineDAO = new DisciplineDAO();
    private final CompetitionDAO competitionDAO = new CompetitionDAO();
    private final ResultatDAO resultatDAO = new ResultatDAO();

    @Override
    public void afficherMenuPrincipal() {
        if (!seConnecter()) {
            System.out.println("Trop de tentatives échouées. Fin du programme.");
            return;
        }
        boolean quitter = false;
        while (!quitter) {
            Utilisateur courant = authService.getUtilisateurConnecte();
            System.out.println("\n===================================");
            System.out.println(" JEUX OLYMPIQUES DE LA JEUNESSE 2026");
            System.out.println(" Connecté: " + courant.getLogin() + " (" + courant.getRole() + ")");
            System.out.println("===================================");
            System.out.println("1. Gestion des utilisateurs");
            System.out.println("2. Gestion des pays");
            System.out.println("3. Gestion des disciplines");
            System.out.println("4. Gestion des athlètes");
            System.out.println("5. Gestion des compétitions");
            System.out.println("6. Gestion des résultats");
            System.out.println("7. Statistiques");
            System.out.println("8. Déconnexion");
            System.out.println("9. Quitter");
            System.out.print("Votre choix : ");

            String choix = sc.nextLine().trim();
            switch (choix) {
                case "1":
                    if (courant.isAdmin()) menuUtilisateurs();
                    else System.out.println("⛔ Accès réservé à l'administrateur.");
                    break;
                case "2": menuPays(); break;
                case "3": menuDisciplines(); break;
                case "4": menuAthletes(); break;
                case "5": menuCompetitions(); break;
                case "6": menuResultats(); break;
                case "7": menuStatistiques(); break;
                case "8":
                    authService.logout();
                    System.out.println("Déconnexion réussie.");
                    if (!seConnecter()) { quitter = true; }
                    break;
                case "9":
                    quitter = true;
                    System.out.println("Au revoir !");
                    break;
                default:
                    System.out.println("Choix invalide.");
            }
        }
    }
    // =========================================================
    // AUTHENTIFICATION
    // =========================================================
    private boolean seConnecter() {
        int tentatives = 0;
        while (tentatives < 3) {
            System.out.println("\n--- CONNEXION ---");
            System.out.print("Login : ");
            String login = sc.nextLine().trim();
            System.out.print("Mot de passe : ");
            String pwd = sc.nextLine().trim();

            Utilisateur u = authService.login(login, pwd);
            if (u != null) {
                System.out.println("Bienvenue " + u.getNomComplet() + " !");
                return true;
            }
            tentatives++;
            System.out.println("❌ Identifiants incorrects. (" + tentatives + "/3)");
        }
        return false;
    }
    // =========================================================
    // 1. GESTION DES UTILISATEURS
    // =========================================================
    private void menuUtilisateurs() {
        boolean retour = false;
        while (!retour) {
            System.out.println("\n--- Gestion des utilisateurs ---");
            System.out.println("1. Ajouter utilisateur");
            System.out.println("2. Modifier utilisateur");
            System.out.println("3. Supprimer utilisateur");
            System.out.println("4. Rechercher utilisateur");
            System.out.println("5. Afficher utilisateurs");
            System.out.println("6. Retour");
            System.out.print("Choix : ");
            switch (sc.nextLine().trim()) {
                case "1": {
                    System.out.print("Nom complet : "); String nom = sc.nextLine();
                    System.out.print("Login : "); String login = sc.nextLine();
                    System.out.print("Mot de passe : "); String pwd = sc.nextLine();
                    System.out.print("Rôle (ADMIN/USER) : "); String role = sc.nextLine();
                    boolean ok = utilisateurDAO.ajouter(new Utilisateur(nom, login, pwd, role));
                    System.out.println(ok ? "✅ Utilisateur ajouté." : "❌ Échec de l'ajout.");
                    break;
                }
                case "2": {
                    System.out.print("Login de l'utilisateur à modifier : ");
                    Utilisateur u = utilisateurDAO.rechercherParLogin(sc.nextLine().trim());
                    if (u == null) { System.out.println("Utilisateur introuvable."); break; }
                    System.out.print("Nouveau nom complet (" + u.getNomComplet() + ") : ");
                    String nom = sc.nextLine(); if (!nom.isBlank()) u.setNomComplet(nom);
                    System.out.print("Nouveau mot de passe : ");
                    String pwd = sc.nextLine(); if (!pwd.isBlank()) u.setMotDePasse(pwd);
                    System.out.print("Nouveau rôle (" + u.getRole() + ") : ");
                    String role = sc.nextLine(); if (!role.isBlank()) u.setRole(role);
                    boolean ok = utilisateurDAO.modifier(u);
                    System.out.println(ok ? "✅ Utilisateur modifié." : "❌ Échec.");
                    break;
                }
                case "3": {
                    System.out.print("Id de l'utilisateur à supprimer : ");
                    int id = lireEntier();
                    boolean ok = utilisateurDAO.supprimer(id);
                    System.out.println(ok ? "✅ Supprimé." : "❌ Échec.");
                    break;
                }
                case "4": {
                    System.out.print("Login recherché : ");
                    Utilisateur u = utilisateurDAO.rechercherParLogin(sc.nextLine().trim());
                    System.out.println(u != null ? u : "Aucun résultat.");
                    break;
                }
                case "5": {
                    List<Utilisateur> liste = utilisateurDAO.listerTous();
                    liste.forEach(System.out::println);
                    break;
                }
                case "6": retour = true; break;
                default: System.out.println("Choix invalide.");
            }
        }
    }
    // =========================================================
    // 2. GESTION DES PAYS
    // =========================================================
    private void menuPays() {
        boolean retour = false;
        while (!retour) {
            System.out.println("\n--- Gestion des pays ---");
            System.out.println("1. Ajouter pays");
            System.out.println("2. Modifier pays");
            System.out.println("3. Supprimer pays");
            System.out.println("4. Rechercher pays");
            System.out.println("5. Liste des pays");
            System.out.println("6. Retour");
            System.out.print("Choix : ");
            switch (sc.nextLine().trim()) {
                case "1": {
                    System.out.print("Nom du pays : "); String nom = sc.nextLine();
                    System.out.print("Continent : "); String continent = sc.nextLine();
                    boolean ok = paysDAO.ajouter(new Pays(nom, continent));
                    System.out.println(ok ? "✅ Pays ajouté." : "❌ Échec.");
                    break;
                }
                case "2": {
                    System.out.print("Id du pays à modifier : "); int id = lireEntier();
                    Pays p = paysDAO.rechercherParId(id);
                    if (p == null) { System.out.println("Pays introuvable."); break; }
                    System.out.print("Nouveau nom (" + p.getNomPays() + ") : ");
                    String nom = sc.nextLine(); if (!nom.isBlank()) p.setNomPays(nom);
                    System.out.print("Nouveau continent (" + p.getContinent() + ") : ");
                    String continent = sc.nextLine(); if (!continent.isBlank()) p.setContinent(continent);
                    boolean ok = paysDAO.modifier(p);
                    System.out.println(ok ? "✅ Modifié." : "❌ Échec.");
                    break;
                }
                case "3": {
                    System.out.print("Id du pays à supprimer : "); int id = lireEntier();
                    boolean ok = paysDAO.supprimer(id);
                    System.out.println(ok ? "✅ Supprimé." : "❌ Échec.");
                    break;
                }
                case "4": {
                    System.out.print("Nom recherché : ");
                    List<Pays> liste = paysDAO.rechercherParNom(sc.nextLine());
                    liste.forEach(System.out::println);
                    if (liste.isEmpty()) System.out.println("Aucun résultat.");
                    break;
                }
                case "5": {
                    paysDAO.listerTous().forEach(System.out::println);
                    break;
                }
                case "6": retour = true; break;
                default: System.out.println("Choix invalide.");
            }
        }
    }
    // =========================================================
    // 3. GESTION DES DISCIPLINES
    // =========================================================
    private void menuDisciplines() {
        boolean retour = false;
        while (!retour) {
            System.out.println("\n--- Gestion des disciplines ---");
            System.out.println("1. Ajouter discipline");
            System.out.println("2. Modifier discipline");
            System.out.println("3. Supprimer discipline");
            System.out.println("4. Rechercher discipline");
            System.out.println("5. Afficher disciplines");
            System.out.println("6. Retour");
            System.out.print("Choix : ");
            switch (sc.nextLine().trim()) {
                case "1": {
                    System.out.print("Nom de la discipline : "); String nom = sc.nextLine();
                    System.out.print("Description : "); String desc = sc.nextLine();
                    boolean ok = disciplineDAO.ajouter(new Discipline(nom, desc));
                    System.out.println(ok ? "✅ Discipline ajoutée." : "❌ Échec.");
                    break;
                }
                case "2": {
                    System.out.print("Id de la discipline à modifier : "); int id = lireEntier();
                    Discipline d = disciplineDAO.rechercherParId(id);
                    if (d == null) { System.out.println("Discipline introuvable."); break; }
                    System.out.print("Nouveau nom (" + d.getNomDiscipline() + ") : ");
                    String nom = sc.nextLine(); if (!nom.isBlank()) d.setNomDiscipline(nom);
                    System.out.print("Nouvelle description (" + d.getDescription() + ") : ");
                    String desc = sc.nextLine(); if (!desc.isBlank()) d.setDescription(desc);
                    boolean ok = disciplineDAO.modifier(d);
                    System.out.println(ok ? "✅ Modifiée." : "❌ Échec.");
                    break;
                }
                case "3": {
                    System.out.print("Id de la discipline à supprimer : "); int id = lireEntier();
                    boolean ok = disciplineDAO.supprimer(id);
                    System.out.println(ok ? "✅ Supprimée." : "❌ Échec.");
                    break;
                }
                case "4": {
                    System.out.print("Id recherché : "); int id = lireEntier();
                    Discipline d = disciplineDAO.rechercherParId(id);
                    System.out.println(d != null ? d : "Aucun résultat.");
                    break;
                }
                case "5": {
                    disciplineDAO.listerTous().forEach(System.out::println);
                    break;
                }
                case "6": retour = true; break;
                default: System.out.println("Choix invalide.");
            }
        }
    }
    // =========================================================
    // 4. GESTION DES ATHLETES
    // =========================================================
    private void menuAthletes() {
        boolean retour = false;
        while (!retour) {
            System.out.println("\n--- Gestion des athlètes ---");
            System.out.println("1. Ajouter athlète");
            System.out.println("2. Modifier athlète");
            System.out.println("3. Supprimer athlète");
            System.out.println("4. Rechercher athlète");
            System.out.println("5. Afficher athlètes");
            System.out.println("6. Retour");
            System.out.print("Choix : ");
            switch (sc.nextLine().trim()) {
                case "1": {
                    System.out.print("Nom : "); String nom = sc.nextLine();
                    System.out.print("Prénom : "); String prenom = sc.nextLine();
                    System.out.print("Sexe (M/F) : "); String sexe = sc.nextLine();
                    LocalDate date = lireDate("Date de naissance (yyyy-MM-dd) : ");
                    System.out.print("Id pays : "); int idPays = lireEntier();
                    System.out.print("Id discipline : "); int idDisc = lireEntier();
                    boolean ok = athleteService.ajouterAthlete(new Athlete(nom, prenom, sexe, date, idPays, idDisc));
                    System.out.println(ok ? "✅ Athlète ajouté." : "❌ Échec.");
                    break;
                }
                case "2": {
                    System.out.print("Id de l'athlète à modifier : "); int id = lireEntier();
                    Athlete a = new AthleteDAO().rechercherParId(id);
                    if (a == null) { System.out.println("Athlète introuvable."); break; }
                    System.out.print("Nouveau nom (" + a.getNom() + ") : ");
                    String nom = sc.nextLine(); if (!nom.isBlank()) a.setNom(nom);
                    System.out.print("Nouveau prénom (" + a.getPrenom() + ") : ");
                    String prenom = sc.nextLine(); if (!prenom.isBlank()) a.setPrenom(prenom);
                    System.out.print("Nouveau sexe (" + a.getSexe() + ") : ");
                    String sexe = sc.nextLine(); if (!sexe.isBlank()) a.setSexe(sexe);
                    System.out.print("Nouvel id pays (" + a.getIdPays() + ") : ");
                    String idPaysStr = sc.nextLine(); if (!idPaysStr.isBlank()) a.setIdPays(Integer.parseInt(idPaysStr));
                    System.out.print("Nouvel id discipline (" + a.getIdDiscipline() + ") : ");
                    String idDiscStr = sc.nextLine(); if (!idDiscStr.isBlank()) a.setIdDiscipline(Integer.parseInt(idDiscStr));
                    boolean ok = athleteService.modifierAthlete(a);
                    System.out.println(ok ? "✅ Modifié." : "❌ Échec.");
                    break;
                }
                case "3": {
                    System.out.print("Id de l'athlète à supprimer : "); int id = lireEntier();
                    boolean ok = athleteService.supprimerAthlete(id);
                    System.out.println(ok ? "✅ Supprimé." : "❌ Échec.");
                    break;
                }
                case "4": {
                    System.out.print("Nom/prénom recherché : ");
                    List<Athlete> liste = athleteService.rechercherAthlete(sc.nextLine());
                    liste.forEach(System.out::println);
                    if (liste.isEmpty()) System.out.println("Aucun résultat.");
                    break;
                }
                case "5": {
                    athleteService.listerAthletes().forEach(System.out::println);
                    break;
                }
                case "6": retour = true; break;
                default: System.out.println("Choix invalide.");
            }
        }
    }

    // =========================================================
    // 5. GESTION DES COMPETITIONS
    // =========================================================
    private void menuCompetitions() {
        boolean retour = false;
        while (!retour) {
            System.out.println("\n--- Gestion des compétitions ---");
            System.out.println("1. Ajouter compétition");
            System.out.println("2. Modifier compétition");
            System.out.println("3. Supprimer compétition");
            System.out.println("4. Rechercher compétition");
            System.out.println("5. Afficher compétitions");
            System.out.println("6. Retour");
            System.out.print("Choix : ");
            switch (sc.nextLine().trim()) {
                case "1": {
                    System.out.print("Nom de la compétition : "); String nom = sc.nextLine();
                    LocalDate date = lireDate("Date (yyyy-MM-dd) : ");
                    System.out.print("Lieu (Dakar/Diamniadio/Saly) : "); String lieu = sc.nextLine();
                    System.out.print("Id discipline : "); int idDisc = lireEntier();
                    boolean ok = competitionDAO.ajouter(new Competition(nom, date, lieu, idDisc));
                    System.out.println(ok ? "✅ Compétition ajoutée." : "❌ Échec.");
                    break;
                }
                case "2": {
                    System.out.print("Id de la compétition à modifier : "); int id = lireEntier();
                    Competition c = competitionDAO.rechercherParId(id);
                    if (c == null) { System.out.println("Compétition introuvable."); break; }
                    System.out.print("Nouveau nom (" + c.getNomCompetition() + ") : ");
                    String nom = sc.nextLine(); if (!nom.isBlank()) c.setNomCompetition(nom);
                    System.out.print("Nouveau lieu (" + c.getLieu() + ") : ");
                    String lieu = sc.nextLine(); if (!lieu.isBlank()) c.setLieu(lieu);
                    System.out.print("Nouvel id discipline (" + c.getIdDiscipline() + ") : ");
                    String idDiscStr = sc.nextLine(); if (!idDiscStr.isBlank()) c.setIdDiscipline(Integer.parseInt(idDiscStr));
                    boolean ok = competitionDAO.modifier(c);
                    System.out.println(ok ? "✅ Modifiée." : "❌ Échec.");
                    break;
                }
                case "3": {
                    System.out.print("Id de la compétition à supprimer : "); int id = lireEntier();
                    boolean ok = competitionDAO.supprimer(id);
                    System.out.println(ok ? "✅ Supprimée." : "❌ Échec.");
                    break;
                }
                case "4": {
                    System.out.print("Nom recherché : ");
                    List<Competition> liste = competitionDAO.rechercherParNom(sc.nextLine());
                    liste.forEach(System.out::println);
                    if (liste.isEmpty()) System.out.println("Aucun résultat.");
                    break;
                }
                case "5": {
                    competitionDAO.listerTous().forEach(System.out::println);
                    break;
                }
                case "6": retour = true; break;
                default: System.out.println("Choix invalide.");
            }
        }
    }
    // =========================================================
    // 6. GESTION DES RESULTATS
    // =========================================================
    private void menuResultats() {
        boolean retour = false;
        while (!retour) {
            System.out.println("\n--- Gestion des résultats ---");
            System.out.println("1. Enregistrer résultat");
            System.out.println("2. Modifier résultat");
            System.out.println("3. Supprimer résultat");
            System.out.println("4. Classement compétition");
            System.out.println("5. Afficher résultats");
            System.out.println("6. Retour");
            System.out.print("Choix : ");
            switch (sc.nextLine().trim()) {
                case "1": {
                    System.out.print("Id athlète : "); int idA = lireEntier();
                    System.out.print("Id compétition : "); int idC = lireEntier();
                    System.out.print("Score : "); double score = lireDouble();
                    System.out.print("Rang : "); int rang = lireEntier();
                    boolean ok = resultatDAO.ajouter(new Resultat(idA, idC, score, rang));
                    System.out.println(ok ? "✅ Résultat enregistré." : "❌ Échec.");
                    break;
                }
                case "2": {
                    System.out.print("Id du résultat à modifier : "); int id = lireEntier();
                    System.out.print("Id athlète : "); int idA = lireEntier();
                    System.out.print("Id compétition : "); int idC = lireEntier();
                    System.out.print("Score : "); double score = lireDouble();
                    System.out.print("Rang : "); int rang = lireEntier();
                    Resultat r = new Resultat(id, idA, idC, score, rang);
                    boolean ok = resultatDAO.modifier(r);
                    System.out.println(ok ? "✅ Modifié." : "❌ Échec.");
                    break;
                }
                case "3": {
                    System.out.print("Id du résultat à supprimer : "); int id = lireEntier();
                    boolean ok = resultatDAO.supprimer(id);
                    System.out.println(ok ? "✅ Supprimé." : "❌ Échec.");
                    break;
                }
                case "4": {
                    System.out.print("Id de la compétition : "); int idC = lireEntier();
                    List<Resultat> classement = resultatDAO.classementParCompetition(idC);
                    if (classement.isEmpty()) System.out.println("Aucun résultat pour cette compétition.");
                    else classement.forEach(System.out::println);
                    break;
                }
                case "5": {
                    resultatDAO.listerTous().forEach(System.out::println);
                    break;
                }
                case "6": retour = true; break;
                default: System.out.println("Choix invalide.");
            }
        }
    }
    // =========================================================
    // 7. STATISTIQUES / TABLEAU DES MEDAILLES
    // =========================================================
    private void menuStatistiques() {
        System.out.println("\n--- Statistiques ---");
        System.out.println("Nombre de pays        : " + statistiqueService.nombrePays());
        System.out.println("Nombre d'athlètes     : " + statistiqueService.nombreAthletes());
        System.out.println("Nombre de disciplines : " + statistiqueService.nombreDisciplines());
        System.out.println("Nombre de compétitions: " + statistiqueService.nombreCompetitions());
        System.out.println("Nombre de résultats   : " + statistiqueService.nombreResultats());

        System.out.println("\n--- Tableau des médailles ---");
        System.out.printf("%-20s %-5s %-8s %-8s %-6s%n", "Pays", "Or", "Argent", "Bronze", "Total");
        for (Object[] ligne : statistiqueService.tableauDesMedailles()) {
            System.out.printf("%-20s %-5s %-8s %-8s %-6s%n", ligne[0], ligne[1], ligne[2], ligne[3], ligne[4]);
        }
    }
    // =========================================================
    // UTILITAIRES DE SAISIE
    // =========================================================
    private int lireEntier() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Veuillez saisir un nombre entier valide : ");
            }
        }
    }
    private double lireDouble() {
        while (true) {
            try {
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Veuillez saisir un nombre valide : ");
            }
        }
    }
    private LocalDate lireDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String saisie = sc.nextLine().trim();
            if (saisie.isBlank()) return null;
            try {
                return LocalDate.parse(saisie, dateFmt);
            } catch (DateTimeParseException e) {
                System.out.println("Format invalide, attendu yyyy-MM-dd.");
            }
        }
    }
}
