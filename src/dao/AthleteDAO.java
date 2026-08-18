package dao;
import model.Athlete;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.Athlete;
public class AthleteDAO {

    private static final String SELECT_JOIN =
            "SELECT a.*, p.nom_pays, d.nom_discipline FROM athlete a " +
                    "LEFT JOIN pays p ON a.id_pays = p.id_pays " +
                    "LEFT JOIN discipline d ON a.id_discipline = d.id_discipline ";

    public boolean ajouter(Athlete a) {
        String sql = "INSERT INTO athlete (nom, prenom, sexe, date_naissance, id_pays, id_discipline) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, a.getNom());
            ps.setString(2, a.getPrenom());
            ps.setString(3, a.getSexe());
            ps.setDate(4, a.getDateNaissance() != null ? Date.valueOf(a.getDateNaissance()) : null);
            ps.setInt(5, a.getIdPays());
            ps.setInt(6, a.getIdDiscipline());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean modifier(Athlete a) {
        String sql = "UPDATE athlete SET nom=?, prenom=?, sexe=?, date_naissance=?, id_pays=?, id_discipline=? WHERE id_athlete=?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, a.getNom());
            ps.setString(2, a.getPrenom());
            ps.setString(3, a.getSexe());
            ps.setDate(4, a.getDateNaissance() != null ? Date.valueOf(a.getDateNaissance()) : null);
            ps.setInt(5, a.getIdPays());
            ps.setInt(6, a.getIdDiscipline());
            ps.setInt(7, a.getIdAthlete());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean supprimer(int id) {
        String sql = "DELETE FROM athlete WHERE id_athlete=?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Athlete rechercherParId(int id) {
        String sql = SELECT_JOIN + " WHERE a.id_athlete=?";
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

    public List<Athlete> rechercherParNom(String nom) {
        List<Athlete> liste = new ArrayList<>();
        String sql = SELECT_JOIN + " WHERE a.nom LIKE ? OR a.prenom LIKE ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, "%" + nom + "%");
            ps.setString(2, "%" + nom + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) liste.add(mapper(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }
    public List<Athlete> listerTous() {
        List<Athlete> liste = new ArrayList<>();
        try (Statement st = Database.getConnection().createStatement();
             ResultSet rs = st.executeQuery(SELECT_JOIN + " ORDER BY a.nom")) {
            while (rs.next()) liste.add(mapper(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }
    public int compter() {
        String sql = "SELECT COUNT(*) FROM athlete";
        try (Statement st = Database.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    private Athlete mapper(ResultSet rs) throws SQLException {
        Athlete a = new Athlete(
                rs.getInt("id_athlete"),
                rs.getString("nom"),
                rs.getString("prenom"),
                rs.getString("sexe"),
                rs.getDate("date_naissance") != null ? rs.getDate("date_naissance").toLocalDate() : null,
                rs.getInt("id_pays"),
                rs.getInt("id_discipline")
        );
        a.setNomPays(rs.getString("nom_pays"));
        a.setNomDiscipline(rs.getString("nom_discipline"));
        return a;
    }
}
