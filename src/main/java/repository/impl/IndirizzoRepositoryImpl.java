package repository.impl;

import model.Indirizzo;
import repository.IndirizzoRepository;
import utils.DBManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IndirizzoRepositoryImpl implements IndirizzoRepository {

    @Override
    public List<Indirizzo> findAll() {
        List<Indirizzo> indirizzi = new ArrayList<>();
        String sql = "SELECT * FROM Indirizzo";

        try (Connection conn = DBManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                indirizzi.add(mapResultSetToIndirizzo(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return indirizzi;
    }

    @Override
    public Optional<Indirizzo> findById(Integer id) {
        String sql = "SELECT * FROM Indirizzo WHERE id_indirizzo = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToIndirizzo(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Indirizzo save(Indirizzo indirizzo) {
        if (indirizzo.getIdIndirizzo() > 0) {
            return update(indirizzo);
        } else {
            return insert(indirizzo);
        }
    }

    private Indirizzo insert(Indirizzo indirizzo) {
        String sql = "INSERT INTO Indirizzo (id_utente, via, citta, provincia, cap, paese, " +
                "nome_destinatario, cognome_destinatario, preferito) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            setIndirizzoParameters(stmt, indirizzo);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        indirizzo.setIdIndirizzo(generatedKeys.getInt(1));
                        return indirizzo;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Indirizzo update(Indirizzo indirizzo) {
        String sql = "UPDATE Indirizzo SET id_utente = ?, via = ?, citta = ?, " +
                "provincia = ?, cap = ?, paese = ?, nome_destinatario = ?, " +
                "cognome_destinatario = ?, preferito = ? WHERE id_indirizzo = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            setIndirizzoParameters(stmt, indirizzo);
            stmt.setInt(10, indirizzo.getIdIndirizzo());

            if (stmt.executeUpdate() > 0) {
                return indirizzo;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM Indirizzo WHERE id_indirizzo = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Indirizzo> findByUtenteId(int idUtente) {
        List<Indirizzo> indirizzi = new ArrayList<>();
        String sql = "SELECT * FROM Indirizzo WHERE id_utente = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUtente);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                indirizzi.add(mapResultSetToIndirizzo(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return indirizzi;
    }

    @Override
    public Optional<Indirizzo> findPreferitoByUtenteId(int idUtente) {
        String sql = "SELECT * FROM Indirizzo WHERE id_utente = ? AND preferito = TRUE";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUtente);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToIndirizzo(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public boolean updatePreferito(int idIndirizzo, boolean preferito) {
        String sql = "UPDATE Indirizzo SET preferito = ? WHERE id_indirizzo = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, preferito);
            stmt.setInt(2, idIndirizzo);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Indirizzo mapResultSetToIndirizzo(ResultSet rs) throws SQLException {
        Indirizzo indirizzo = new Indirizzo();
        indirizzo.setIdIndirizzo(rs.getInt("id_indirizzo"));
        indirizzo.setIdUtente(rs.getInt("id_utente"));
        indirizzo.setVia(rs.getString("via"));
        indirizzo.setCitta(rs.getString("citta"));
        indirizzo.setProvincia(rs.getString("provincia"));
        indirizzo.setCap(rs.getString("cap"));
        indirizzo.setPaese(rs.getString("paese"));
        indirizzo.setNomeDestinatario(rs.getString("nome_destinatario"));
        indirizzo.setCognomeDestinatario(rs.getString("cognome_destinatario"));
        indirizzo.setPreferito(rs.getBoolean("preferito"));
        return indirizzo;
    }

    private void setIndirizzoParameters(PreparedStatement stmt, Indirizzo indirizzo) throws SQLException {
        stmt.setInt(1, indirizzo.getIdUtente());
        stmt.setString(2, indirizzo.getVia());
        stmt.setString(3, indirizzo.getCitta());
        stmt.setString(4, indirizzo.getProvincia());
        stmt.setString(5, indirizzo.getCap());
        stmt.setString(6, indirizzo.getPaese());
        stmt.setString(7, indirizzo.getNomeDestinatario());
        stmt.setString(8, indirizzo.getCognomeDestinatario());
        stmt.setBoolean(9, indirizzo.isPreferito());
    }
}