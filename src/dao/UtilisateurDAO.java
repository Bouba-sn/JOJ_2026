package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Utilisateur;
public class UtilisateurDAO {

    public boolean ajouter(Utilisateur u) {
        String sql = "INSERT INTO utilisateur (nom_complet, login, mot_de_passe, role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, u.getNomComplet());
            ps.setString(2, u.getLogin());
            ps.setString(3, u.getMotDePasse());
            ps.setString(4, u.getRole());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean modifier(Utilisateur u) {
        String sql = "UPDATE utilisateur SET nom_complet=?, login=?, mot_de_passe=?, role=? WHERE id_utilisateur=?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, u.getNomComplet());
            ps.setString(2, u.getLogin());
            ps.setString(3, u.getMotDePasse());
            ps.setString(4, u.getRole());
            ps.setInt(5, u.getIdUtilisateur());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean supprimer(int id) {
        String sql = "DELETE FROM utilisateur WHERE id_utilisateur=?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Utilisateur rechercherParLogin(String login) {
        String sql = "SELECT * FROM utilisateur WHERE login=?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, login);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapper(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** Vérifie le login/mot de passe pour l'authentification. */
    public Utilisateur authentifier(String login, String motDePasse) {
        String sql = "SELECT * FROM utilisateur WHERE login=? AND mot_de_passe=?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setString(2, motDePasse);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapper(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Utilisateur> listerTous() {
        List<Utilisateur> liste = new ArrayList<>();
        String sql = "SELECT * FROM utilisateur";
        try (Statement st = Database.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) liste.add(mapper(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }

    private Utilisateur mapper(ResultSet rs) throws SQLException {
        return new Utilisateur(
                rs.getInt("id_utilisateur"),
                rs.getString("nom_complet"),
                rs.getString("login"),
                rs.getString("mot_de_passe"),
                rs.getString("role")
        );
    }
}
