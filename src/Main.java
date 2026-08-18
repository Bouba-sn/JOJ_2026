import dao.Database;
import service.IMenu;
import service.IMenuImple;
public class Main {
    public static void main(String[] args) {
        System.out.println("Démarrage de l'application JOJ Dakar 2026...");
        if (Database.getConnection() == null) {
            System.out.println("Impossible de se connecter à la base de données. Vérifie ta configuration (dao/Database.java).");
            return;
        }
        IMenu menu = new IMenuImple();
        menu.afficherMenuPrincipal();
        Database.closeConnection();
        System.out.println("Application terminée.");
    }
}
