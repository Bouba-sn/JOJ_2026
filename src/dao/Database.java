package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe utilitaire responsable de la connexion JDBC à la base MySQL.
 *
 * ETAPE IMPORTANTE : adapte URL, USER et PASSWORD à ta configuration MySQL locale.
 */
public class Database {

    // ----- A ADAPTER SELON TON ENVIRONNEMENT -----
    private static final String URL =
            "jdbc:mysql://localhost:3306/joj_dakar2026?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "root"; // mets ton propre mot de passe MySQL
    // -----------------------------------------------

    private static Connection connection;

    // Empêche l'instanciation
    private Database() {}

    /**
     * Retourne une connexion unique (singleton) vers la base de données.
     * Si la connexion est fermée ou inexistante, elle est recréée.
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // Charge explicitement le driver (utile selon la version du connector)
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Connexion à la base 'joj_dakar2026' réussie.");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("❌ Driver MySQL introuvable. As-tu ajouté le connector JDBC au projet ?");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("❌ Échec de connexion à la base de données : " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    /** Ferme proprement la connexion (à appeler en quittant l'application). */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connexion fermée.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Petit programme de test de connexion (peut être lancé indépendamment). */
    public static void main(String[] args) {
        Connection c = Database.getConnection();
        if (c != null) {
            System.out.println("Test de connexion : OK");
        } else {
            System.out.println("Test de connexion : ECHEC");
        }
        closeConnection();
    }
}
