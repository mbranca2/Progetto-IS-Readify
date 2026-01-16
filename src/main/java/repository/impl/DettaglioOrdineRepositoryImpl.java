package repository.impl;

import model.DettaglioOrdine;
import repository.DettaglioOrdineRepository;
import utils.DBManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DettaglioOrdineRepositoryImpl implements DettaglioOrdineRepository {
    @Override
    public List<DettaglioOrdine> findByOrdineId(int ordineId) {
        List<DettaglioOrdine> dettagli = new ArrayList<>();
        String sql = "SELECT c.id_ordine, c.id_libro, c.quantita, c.prezzo_unitario, " +
                "l.titolo, l.autore, l.isbn, l.copertina as immagine_copertina " +
                "FROM Contiene c " +
                "JOIN Libro l ON c.id_libro = l.id_libro " +
                "WHERE c.id_ordine = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ordineId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                DettaglioOrdine dettaglio = mapResultSetToDettaglio(rs);
                dettagli.add(dettaglio);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dettagli;
    }

    @Override
    public boolean saveAll(List<DettaglioOrdine> dettagli) {
        if (dettagli == null || dettagli.isEmpty()) {
            return false;
        }

        String sql = "INSERT INTO Contiene (id_ordine, id_libro, quantita, prezzo_unitario) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);

            for (DettaglioOrdine dettaglio : dettagli) {
                stmt.setInt(1, dettaglio.getIdOrdine());
                stmt.setInt(2, dettaglio.getIdLibro());
                stmt.setInt(3, dettaglio.getQuantita());
                stmt.setBigDecimal(4, dettaglio.getPrezzoUnitario());
                stmt.addBatch();
            }

            int[] results = stmt.executeBatch();
            conn.commit();

            // Verifica che tutti gli inserimenti siano andati a buon fine
            for (int result : results) {
                if (result <= 0) {
                    return false;
                }
            }
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try (Connection conn = DBManager.getConnection()) {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        }
    }

    @Override
    public Optional<DettaglioOrdine> findById(Integer id) {
        // Not implemented as DettaglioOrdine doesn't have a direct ID in the database
        // The ID is a combination of id_ordine and id_libro
        return Optional.empty();
    }

    @Override
    public List<DettaglioOrdine> findAll() {
        // This might not be needed as we usually query by order ID
        return new ArrayList<>();
    }

    @Override
    public DettaglioOrdine save(DettaglioOrdine entity) {
        // Since we're using saveAll for batch operations, this can be a simple wrapper
        List<DettaglioOrdine> list = new ArrayList<>();
        list.add(entity);
        return saveAll(list) ? entity : null;
    }

    @Override
    public void delete(Integer id) {
        // Not implemented as deletion is typically handled at the order level
    }

    private DettaglioOrdine mapResultSetToDettaglio(ResultSet rs) throws SQLException {
        DettaglioOrdine dettaglio = new DettaglioOrdine();

        // Create a composite ID since there's no single ID in the database
        int idOrdine = rs.getInt("id_ordine");
        int idLibro = rs.getInt("id_libro");
        dettaglio.setId(idOrdine * 1000 + idLibro); // Create a unique ID

        dettaglio.setIdOrdine(idOrdine);
        dettaglio.setIdLibro(idLibro);
        dettaglio.setQuantita(rs.getInt("quantita"));
        dettaglio.setPrezzoUnitario(rs.getBigDecimal("prezzo_unitario"));

        // Additional book details
        dettaglio.setTitoloLibro(rs.getString("titolo"));
        dettaglio.setAutoreLibro(rs.getString("autore"));
        dettaglio.setIsbnLibro(rs.getString("isbn"));

        // Handle book cover image path
        String copertina = rs.getString("immagine_copertina");
        if (copertina != null && !copertina.startsWith("img/libri/copertine/")) {
            copertina = "img/libri/copertine/" + copertina;
        }
        dettaglio.setImmagineCopertina(copertina);

        return dettaglio;
    }
}