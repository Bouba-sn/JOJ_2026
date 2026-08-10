package service;

import dao.UtilisateurDAO;
import model.Utilisateur;

public class IAuthServiceImple implements IAuthService {

    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
    private Utilisateur utilisateurConnecte;

    @Override
    public Utilisateur login(String login, String motDePasse) {
        Utilisateur u = utilisateurDAO.authentifier(login, motDePasse);
        if (u != null) {
            this.utilisateurConnecte = u;
        }
        return u;
    }

    @Override
    public void logout() {
        this.utilisateurConnecte = null;
    }

    @Override
    public Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }
}
