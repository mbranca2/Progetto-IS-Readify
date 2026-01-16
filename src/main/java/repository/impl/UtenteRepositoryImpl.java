package repository.impl;

import model.Utente;
import repository.UtenteRepository;
import utils.DBManager;

import java.sql.*;
import java.util.Optional;

public class UtenteRepositoryImpl implements UtenteRepository {

    @Override
    public Optional<Utente> findById(Integer id) {
        String query = "SELECT * FROM Utente WHERE id = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToUtente(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Utente> findByEmail(String email) {
        String query = "SELECT * FROM Utente WHERE email = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToUtente(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public boolean existsByEmail(String email) {
        String query = "SELECT COUNT(*) FROM Utente WHERE email = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Utente save(Utente utente) {
        if (utente.getId() == null) {
            return insert(utente);
        } else {
            return update(utente);
        }
    }

    private Utente insert(Utente utente) {
        String query = "INSERT INTO Utente (email, password, nome, cognome, admin) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            setUtenteParameters(stmt, utente);
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    utente.setId(generatedKeys.getInt(1));
                }
            }
            return utente;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Utente update(Utente utente) {
        String query = "UPDATE Utente SET email=?, password=?, nome=?, cognome=?, admin=? WHERE id=?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            setUtenteParameters(stmt, utente);
            stmt.setInt(6, utente.getId());
            stmt.executeUpdate();

            return utente;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void delete(Integer id) {
        String query = "DELETE FROM Utente WHERE id = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Utente mapResultSetToUtente(ResultSet rs) throws SQLException {
        Utente utente = new Utente();
        utente.setId(rs.getInt("id"));
        utente.setEmail(rs.getString("email"));
        utente.setPassword(rs.getString("password"));
        utente.setNome(rs.getString("nome"));
        utente.setCognome(rs.getString("cognome"));
        utente.setAdmin(rs.getBoolean("admin"));
        return utente;
    }

    private void setUtenteParameters(PreparedStatement stmt, Utente utente) throws SQLException {
        stmt.setString(1, utente.getEmail());
        stmt.setString(2, utente.getPassword());
        stmt.setString(3, utente.getNome());
        stmt.setString(4, utente.getCognome());
        stmt.setBoolean(5, utente.isAdmin());
    }
}