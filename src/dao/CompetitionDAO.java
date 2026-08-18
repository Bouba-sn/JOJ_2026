package dao;
import model.Competition;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Competition;
public class CompetitionDAO {
    private static final String SELECT_JOIN =
            "SELECT c.*, d.nom_discipline FROM competition c " +
                    "LEFT JOIN discipline d ON c.id_discipline = d.id_discipline ";
    public boolean ajouter(Competition c) {
        String sql = "INSERT INTO competition (nom_competition, date_competition, lieu, id_discipline) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, c.getNomCompetition());
            ps.setDate(2, c.getDateCompetition() != null ? Date.valueOf(c.getDateCompetition()) : null);
            ps.setString(3, c.getLieu());
            ps.setInt(4, c.getIdDiscipline());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean modifier(Competition c) {
        String sql = "UPDATE competition SET nom_competition=?, date_competition=?, lieu=?, id_discipline=? WHERE id_competition=?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, c.getNomCompetition());
            ps.setDate(2, c.getDateCompetition() != null ? Date.valueOf(c.getDateCompetition()) : null);
            ps.setString(3, c.getLieu());
            ps.setInt(4, c.getIdDiscipline());
            ps.setInt(5, c.getIdCompetition());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean supprimer(int id) {
        String sql = "DELETE FROM competition WHERE id_competition=?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public Competition rechercherParId(int id) {
        String sql = SELECT_JOIN + " WHERE c.id_competition=?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapper(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<Competition> rechercherParNom(String nom) {
        List<Competition> liste = new ArrayList<>();
        String sql = SELECT_JOIN + " WHERE c.nom_competition LIKE ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, "%" + nom + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) liste.add(mapper(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }
    public List<Competition> listerTous() {
        List<Competition> liste = new ArrayList<>();
        try (Statement st = Database.getConnection().createStatement();
             ResultSet rs = st.executeQuery(SELECT_JOIN + " ORDER BY c.date_competition")) {
            while (rs.next()) liste.add(mapper(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }
    public int compter() {
        String sql = "SELECT COUNT(*) FROM competition";
        try (Statement st = Database.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    private Competition mapper(ResultSet rs) throws SQLException {
        Competition c = new Competition(
                rs.getInt("id_competition"),
                rs.getString("nom_competition"),
                rs.getDate("date_competition") != null ? rs.getDate("date_competition").toLocalDate() : null,
                rs.getString("lieu"),
                rs.getInt("id_discipline")
        );
        c.setNomDiscipline(rs.getString("nom_discipline"));
        return c;
    }
}
