package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Resultat;
public class ResultatDAO {

    private static final String SELECT_JOIN =
            "SELECT r.*, CONCAT(a.prenom, ' ', a.nom) AS nom_athlete, c.nom_competition " +
                    "FROM resultat r " +
                    "LEFT JOIN athlete a ON r.id_athlete = a.id_athlete " +
                    "LEFT JOIN competition c ON r.id_competition = c.id_competition ";

    public boolean ajouter(Resultat r) {
        String sql = "INSERT INTO resultat (id_athlete, id_competition, score, rang) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, r.getIdAthlete());
            ps.setInt(2, r.getIdCompetition());
            ps.setDouble(3, r.getScore());
            ps.setInt(4, r.getRang());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean modifier(Resultat r) {
        String sql = "UPDATE resultat SET id_athlete=?, id_competition=?, score=?, rang=? WHERE id_resultat=?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, r.getIdAthlete());
            ps.setInt(2, r.getIdCompetition());
            ps.setDouble(3, r.getScore());
            ps.setInt(4, r.getRang());
            ps.setInt(5, r.getIdResultat());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean supprimer(int id) {
        String sql = "DELETE FROM resultat WHERE id_resultat=?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Resultat> listerTous() {
        List<Resultat> liste = new ArrayList<>();
        try (Statement st = Database.getConnection().createStatement();
             ResultSet rs = st.executeQuery(SELECT_JOIN)) {
            while (rs.next()) liste.add(mapper(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }

    /** Classement d'une compétition : trie les résultats par rang croissant. */
    public List<Resultat> classementParCompetition(int idCompetition) {
        List<Resultat> liste = new ArrayList<>();
        String sql = SELECT_JOIN + " WHERE r.id_competition = ? ORDER BY r.rang ASC";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, idCompetition);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) liste.add(mapper(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }

    public int compter() {
        String sql = "SELECT COUNT(*) FROM resultat";
        try (Statement st = Database.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Tableau des médailles : pour chaque pays, compte le nombre de médailles
     * Or (rang=1), Argent (rang=2), Bronze (rang=3) obtenues par ses athlètes.
     */
    public List<Object[]> tableauDesMedailles() {
        List<Object[]> tableau = new ArrayList<>();
        String sql =
                "SELECT p.nom_pays, " +
                        "SUM(CASE WHEN r.rang = 1 THEN 1 ELSE 0 END) AS or_, " +
                        "SUM(CASE WHEN r.rang = 2 THEN 1 ELSE 0 END) AS argent, " +
                        "SUM(CASE WHEN r.rang = 3 THEN 1 ELSE 0 END) AS bronze " +
                        "FROM resultat r " +
                        "JOIN athlete a ON r.id_athlete = a.id_athlete " +
                        "JOIN pays p ON a.id_pays = p.id_pays " +
                        "GROUP BY p.nom_pays " +
                        "ORDER BY or_ DESC, argent DESC, bronze DESC";
        try (Statement st = Database.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                String pays = rs.getString("nom_pays");
                int or = rs.getInt("or_");
                int argent = rs.getInt("argent");
                int bronze = rs.getInt("bronze");
                int total = or + argent + bronze;
                tableau.add(new Object[]{pays, or, argent, bronze, total});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tableau;
    }

    private Resultat mapper(ResultSet rs) throws SQLException {
        Resultat r = new Resultat(
                rs.getInt("id_resultat"),
                rs.getInt("id_athlete"),
                rs.getInt("id_competition"),
                rs.getDouble("score"),
                rs.getInt("rang")
        );
        r.setNomAthlete(rs.getString("nom_athlete"));
        r.setNomCompetition(rs.getString("nom_competition"));
        return r;
    }
}
