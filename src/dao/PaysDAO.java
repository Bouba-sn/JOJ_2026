package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Pays;
public class PaysDAO {

    public boolean ajouter(Pays p) {
        String sql = "INSERT INTO pays (nom_pays, continent) VALUES (?, ?)";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, p.getNomPays());
            ps.setString(2, p.getContinent());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean modifier(Pays p) {
        String sql = "UPDATE pays SET nom_pays=?, continent=? WHERE id_pays=?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, p.getNomPays());
            ps.setString(2, p.getContinent());
            ps.setInt(3, p.getIdPays());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean supprimer(int id) {
        String sql = "DELETE FROM pays WHERE id_pays=?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Pays rechercherParId(int id) {
        String sql = "SELECT * FROM pays WHERE id_pays=?";
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

    public List<Pays> rechercherParNom(String nom) {
        List<Pays> liste = new ArrayList<>();
        String sql = "SELECT * FROM pays WHERE nom_pays LIKE ?";
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

    public List<Pays> listerTous() {
        List<Pays> liste = new ArrayList<>();
        String sql = "SELECT * FROM pays ORDER BY nom_pays";
        try (Statement st = Database.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) liste.add(mapper(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }

    public int compter() {
        String sql = "SELECT COUNT(*) FROM pays";
        try (Statement st = Database.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Pays mapper(ResultSet rs) throws SQLException {
        return new Pays(rs.getInt("id_pays"), rs.getString("nom_pays"), rs.getString("continent"));
    }
}
