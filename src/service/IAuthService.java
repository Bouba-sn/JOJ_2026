package service;
import model.Utilisateur;

public interface IAuthService {
    /** Tente de connecter l'utilisateur ; renvoie l'utilisateur si succès, null sinon. */
    Utilisateur login(String login, String motDePasse);

    /** Déconnecte l'utilisateur courant. */
    void logout();

    /** Renvoie l'utilisateur actuellement connecté (ou null). */
    Utilisateur getUtilisateurConnecte();
}
