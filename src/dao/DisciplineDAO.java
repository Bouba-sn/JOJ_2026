package dao;

import model.Discipline;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Discipline;
public class DisciplineDAO {

    public boolean ajouter(Discipline d) {
        String sql = "INSERT INTO discipline (nom_discipline, description) VALUES (?, ?)";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, d.getNomDiscipline());
            ps.setString(2, d.getDescription());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean modifier(Discipline d) {
        String sql = "UPDATE discipline SET nom_discipline=?, description=? WHERE id_discipline=?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, d.getNomDiscipline());
            ps.setString(2, d.getDescription());
            ps.setInt(3, d.getIdDiscipline());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean supprimer(int id) {
        String sql = "DELETE FROM discipline WHERE id_discipline=?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Discipline rechercherParId(int id) {
        String sql = "SELECT * FROM discipline WHERE id_discipline=?";
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

    public List<Discipline> listerTous() {
        List<Discipline> liste = new ArrayList<>();
        String sql = "SELECT * FROM discipline ORDER BY nom_discipline";
        try (Statement st = Database.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) liste.add(mapper(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }

    public int compter() {
        String sql = "SELECT COUNT(*) FROM discipline";
        try (Statement st = Database.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Discipline mapper(ResultSet rs) throws SQLException {
        return new Discipline(rs.getInt("id_discipline"), rs.getString("nom_discipline"), rs.getString("description"));
    }
}
