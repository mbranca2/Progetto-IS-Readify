package repository.impl;

import model.Ordine;
import repository.OrdineRepository;
import utils.DBManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrdineRepositoryImpl implements OrdineRepository {

    @Override
    public List<Ordine> findAll() {
        List<Ordine> ordini = new ArrayList<>();
        String query = "SELECT * FROM Ordine ORDER BY data_ordine DESC";

        try (Connection conn = DBManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                ordini.add(mapResultSetToOrdine(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ordini;
    }

    @Override
    public Optional<Ordine> findById(Integer id) {
        String query = "SELECT * FROM Ordine WHERE id_ordine = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToOrdine(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Ordine> findByUtenteId(int utenteId) {
        List<Ordine> ordini = new ArrayList<>();
        String query = "SELECT * FROM Ordine WHERE id_utente = ? ORDER BY data_ordine DESC";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, utenteId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ordini.add(mapResultSetToOrdine(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ordini;
    }

    @Override
    public Ordine save(Ordine ordine) {
        if (ordine.getIdOrdine() == 0) {
            return insert(ordine);
        } else {
            return update(ordine);
        }
    }

    private Ordine insert(Ordine ordine) {
        String query = "INSERT INTO Ordine (id_utente, id_indirizzo, data_ordine, stato, totale) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            setOrdineParameters(stmt, ordine);
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    ordine.setIdOrdine(generatedKeys.getInt(1));
                }
            }
            return ordine;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Ordine update(Ordine ordine) {
        String query = "UPDATE Ordine SET id_utente=?, id_indirizzo=?, data_ordine=?, stato=?, totale=? WHERE id_ordine=?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            setOrdineParameters(stmt, ordine);
            stmt.setInt(6, ordine.getIdOrdine());
            stmt.executeUpdate();

            return ordine;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean updateStato(int idOrdine, String nuovoStato) {
        String query = "UPDATE Ordine SET stato = ? WHERE id_ordine = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, nuovoStato);
            stmt.setInt(2, idOrdine);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void delete(Integer id) {
        String query = "DELETE FROM Ordine WHERE id_ordine = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Ordine mapResultSetToOrdine(ResultSet rs) throws SQLException {
        Ordine ordine = new Ordine();
        ordine.setIdOrdine(rs.getInt("id_ordine"));
        ordine.setIdUtente(rs.getInt("id_utente"));
        ordine.setIdIndirizzo(rs.getInt("id_indirizzo"));
        ordine.setDataOrdine(rs.getDate("data_ordine"));
        ordine.setStato(rs.getString("stato"));
        ordine.setTotale(rs.getBigDecimal("totale"));
        return ordine;
    }

    private void setOrdineParameters(PreparedStatement stmt, Ordine ordine) throws SQLException {
        stmt.setInt(1, ordine.getIdUtente());
        stmt.setInt(2, ordine.getIdIndirizzo());
        stmt.setDate(3, ordine.getDataOrdine());
        stmt.setString(4, ordine.getStato());
        stmt.setBigDecimal(5, ordine.getTotale());
    }
}