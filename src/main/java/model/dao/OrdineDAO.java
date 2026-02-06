package model.dao;

import model.bean.DettaglioOrdine;
import model.bean.Ordine;
import model.bean.StatoOrdine;
import utils.DBManager;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrdineDAO {
    private static final Logger logger = Logger.getLogger(OrdineDAO.class.getName());

    public boolean salvaOrdine(Connection conn, Ordine ordine) throws SQLException {
        if (ordine == null) {
            return false;
        }

        if (ordine.getIdOrdine() > 0) {
            return aggiornaOrdine(conn, ordine);
        } else {
            return inserisciNuovoOrdine(conn, ordine);
        }
    }

    private boolean inserisciNuovoOrdine(Connection conn, Ordine ordine) throws SQLException {
        String queryOrdine = "INSERT INTO Ordine (id_utente, id_indirizzo, stato, totale) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmtOrdine = conn.prepareStatement(queryOrdine, Statement.RETURN_GENERATED_KEYS)) {
            stmtOrdine.setInt(1, ordine.getIdUtente());

            if (ordine.getIdIndirizzo() > 0) {
                stmtOrdine.setInt(2, ordine.getIdIndirizzo());
            } else {
                stmtOrdine.setNull(2, Types.INTEGER);
            }

            String stato = (ordine.getStato() != null ? ordine.getStato() : StatoOrdine.IN_ATTESA).toDbValue();
            stmtOrdine.setString(3, stato);
            stmtOrdine.setBigDecimal(4, ordine.getTotale() != null ? ordine.getTotale() : BigDecimal.ZERO);

            int righeInserite = stmtOrdine.executeUpdate();
            if (righeInserite <= 0) {
                return false;
            }

            try (ResultSet rs = stmtOrdine.getGeneratedKeys()) {
                if (!rs.next()) {
                    return false;
                }

                int idOrdine = rs.getInt(1);
                ordine.setIdOrdine(idOrdine);

                if (ordine.getDettagli() != null && !ordine.getDettagli().isEmpty()) {
                    return inserisciDettagliOrdine(conn, ordine);
                }

                return true;
            }
        }
    }

    private boolean aggiornaOrdine(Connection conn, Ordine ordine) throws SQLException {
        String query = "UPDATE Ordine SET id_utente = ?, id_indirizzo = ?, stato = ?, totale = ? WHERE id_ordine = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, ordine.getIdUtente());

            if (ordine.getIdIndirizzo() > 0) {
                stmt.setInt(2, ordine.getIdIndirizzo());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }

            String stato = (ordine.getStato() != null ? ordine.getStato() : StatoOrdine.IN_ATTESA).toDbValue();
            stmt.setString(3, stato);
            stmt.setBigDecimal(4, ordine.getTotale() != null ? ordine.getTotale() : BigDecimal.ZERO);
            stmt.setInt(5, ordine.getIdOrdine());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated <= 0) {
                return false;
            }

            if (ordine.getDettagli() != null && !ordine.getDettagli().isEmpty()) {
                String deleteQuery = "DELETE FROM Contiene WHERE id_ordine = ?";
                try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
                    deleteStmt.setInt(1, ordine.getIdOrdine());
                    deleteStmt.executeUpdate();
                }
                return inserisciDettagliOrdine(conn, ordine);
            }

            return true;
        }
    }

    private boolean inserisciDettagliOrdine(Connection conn, Ordine ordine) throws SQLException {
        String query = "INSERT INTO Contiene (id_ordine, id_libro, quantita, prezzo_unitario) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            for (DettaglioOrdine dettaglio : ordine.getDettagli()) {
                stmt.setInt(1, ordine.getIdOrdine());
                stmt.setInt(2, dettaglio.getIdLibro());
                stmt.setInt(3, dettaglio.getQuantita());
                stmt.setBigDecimal(4, dettaglio.getPrezzoUnitario());
                stmt.addBatch();

                if (!aggiornaDisponibilitaLibro(conn, dettaglio.getIdLibro(), -dettaglio.getQuantita())) {
                    return false;
                }
            }
            stmt.executeBatch();
            return true;
        }
    }

    public List<Ordine> trovaPerIdUtente(int idUtente) {
        List<Ordine> ordini = new ArrayList<>();
        String sql = "SELECT o.*, u.nome, u.cognome, u.email, u.id_utente, " +
                "i.id_indirizzo, i.via, i.citta, i.cap, i.provincia, i.paese " +
                "FROM Ordine o " +
                "JOIN Utente u ON o.id_utente = u.id_utente " +
                "LEFT JOIN Indirizzo i ON o.id_indirizzo = i.id_indirizzo " +
                "WHERE o.id_utente = ? " +
                "ORDER BY o.data_ordine DESC";

        try (Connection conn = DBManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUtente);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Ordine ordine = mappaOrdineDaResultSet(rs);
                caricaDettagliOrdine(ordine, conn);
                ordini.add(ordine);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore durante il recupero degli ordini dell'utente con ID: " + idUtente, e);
        }
        return ordini;
    }

    public List<Ordine> trovaTuttiOrdini() {
        List<Ordine> ordini = new ArrayList<>();

        String sql = "SELECT o.* " +
                "FROM Ordine o " +
                "ORDER BY o.data_ordine DESC";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Ordine ordine = mappaOrdineDaResultSet(rs);
                ordini.add(ordine);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore durante il recupero di tutti gli ordini (admin).", e);
        }

        return ordini;
    }

    public Ordine findById(Connection conn, int idOrdine) throws SQLException {
        String sql = "SELECT * FROM Ordine WHERE id_ordine = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idOrdine);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) return null;
                Ordine o = new Ordine();
                o.setIdOrdine(rs.getInt("id_ordine"));
                o.setIdUtente(rs.getInt("id_utente"));
                int idInd = rs.getInt("id_indirizzo");
                if (!rs.wasNull()) o.setIdIndirizzo(idInd);
                o.setTotale(rs.getBigDecimal("totale"));
                o.setStato(StatoOrdine.fromDbValue(rs.getString("stato")));
                Timestamp ts = rs.getTimestamp("data_ordine");
                o.setDataOrdine(ts != null ? new Date(ts.getTime()) : null);
                return o;
            }
        }
    }

    public boolean cancelIfPending(Connection conn, int idOrdine, int idUtente) throws SQLException {
        String sql = "UPDATE Ordine SET stato = ? WHERE id_ordine = ? AND id_utente = ? AND stato = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, StatoOrdine.ANNULLATO.toDbValue());
            stmt.setInt(2, idOrdine);
            stmt.setInt(3, idUtente);
            stmt.setString(4, StatoOrdine.IN_ATTESA.toDbValue());
            return stmt.executeUpdate() > 0;
        }
    }

    public void restoreStockForOrder(Connection conn, int idOrdine) throws SQLException {
        String sql = "SELECT id_libro, quantita FROM Contiene WHERE id_ordine = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idOrdine);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int idLibro = rs.getInt("id_libro");
                    int qta = rs.getInt("quantita");
                    aggiornaDisponibilitaLibro(conn, idLibro, qta);
                }
            }
        }
    }

    private Ordine mappaOrdineDaResultSet(ResultSet rs) throws SQLException {
        Ordine ordine = new Ordine();
        ordine.setIdOrdine(rs.getInt("id_ordine"));
        Timestamp timestamp = rs.getTimestamp("data_ordine");
        ordine.setDataOrdine(timestamp != null ? new Date(timestamp.getTime()) : null);

        ordine.setStato(StatoOrdine.fromDbValue(rs.getString("stato")));
        ordine.setTotale(rs.getBigDecimal("totale"));

        try {
            if (hasColumn(rs, "id_utente")) {
                ordine.setIdUtente(rs.getInt("id_utente"));
            }
            if (hasColumn(rs, "id_indirizzo") && !rs.wasNull()) {
                ordine.setIdIndirizzo(rs.getInt("id_indirizzo"));
            }
        } catch (SQLException e) {
            logger.log(Level.FINE, "Errore durante il mapping dei dettagli aggiuntivi", e);
        }
        return ordine;
    }

    private DettaglioOrdine mappaRisultatoADettaglio(ResultSet rs) throws SQLException {
        DettaglioOrdine dettaglio = new DettaglioOrdine();
        dettaglio.setId(rs.getInt("id_ordine") * 1000 + rs.getInt("id_libro"));
        dettaglio.setIdOrdine(rs.getInt("id_ordine"));
        dettaglio.setIdLibro(rs.getInt("id_libro"));
        dettaglio.setQuantita(rs.getInt("quantita"));
        dettaglio.setPrezzoUnitario(rs.getBigDecimal("prezzo_unitario"));

        try {
            if (hasColumn(rs, "titolo")) dettaglio.setTitoloLibro(rs.getString("titolo"));
            if (hasColumn(rs, "autore")) dettaglio.setAutoreLibro(rs.getString("autore"));
            if (hasColumn(rs, "isbn")) dettaglio.setIsbnLibro(rs.getString("isbn"));

            if (hasColumn(rs, "immagine_copertina")) {
                String copertina = rs.getString("immagine_copertina");
                if (copertina != null && !copertina.trim().isEmpty()) {
                    if (!copertina.startsWith("img/libri/copertine/")) {
                        copertina = "img/libri/copertine/" + copertina;
                    }
                    dettaglio.setImmagineCopertina(copertina);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Errore durante il mapping dei dettagli aggiuntivi", e);
        }
        return dettaglio;
    }

    private boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        int columns = meta.getColumnCount();
        for (int x = 1; x <= columns; x++) {
            if (columnName.equalsIgnoreCase(meta.getColumnName(x))) {
                return true;
            }
        }
        return false;
    }

    private void caricaDettagliOrdine(Ordine ordine, Connection conn) throws SQLException {
        String sql = "SELECT c.id_ordine, c.id_libro, c.quantita, c.prezzo_unitario, " +
                "l.titolo, l.autore, l.isbn, l.copertina as immagine_copertina " +
                "FROM Contiene c " +
                "JOIN Libro l ON c.id_libro = l.id_libro " +
                "WHERE c.id_ordine = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ordine.getIdOrdine());
            try (ResultSet rs = stmt.executeQuery()) {
                List<DettaglioOrdine> dettagli = new ArrayList<>();
                while (rs.next()) {
                    dettagli.add(mappaRisultatoADettaglio(rs));
                }
                ordine.setDettagli(dettagli);
            }
        }
    }

    public boolean hasUserPurchasedBook(int idUtente, int idLibro) {
        if (idUtente <= 0 || idLibro <= 0) return false;

        String sql = "SELECT 1 " +
                "FROM Ordine o " +
                "JOIN Contiene c ON o.id_ordine = c.id_ordine " +
                "WHERE o.id_utente = ? AND c.id_libro = ? AND o.stato <> 'annullato' " +
                "LIMIT 1";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUtente);
            stmt.setInt(2, idLibro);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore durante la verifica acquisto libro. idUtente=" + idUtente +
                    ", idLibro=" + idLibro, e);
            return false;
        }
    }

    private boolean aggiornaDisponibilitaLibro(Connection conn, int idLibro, int quantitaDaAggiornare) throws SQLException {
        String checkQuery = "SELECT disponibilita FROM Libro WHERE id_libro = ? FOR UPDATE";
        String updateQuery = "UPDATE Libro SET disponibilita = ? WHERE id_libro = ?";

        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
             PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {

            checkStmt.setInt(1, idLibro);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                logger.log(Level.WARNING, "Libro con ID " + idLibro + " non trovato");
                return false;
            }

            int quantitaAttuale = rs.getInt("disponibilita");
            int nuovaQuantita = quantitaAttuale + quantitaDaAggiornare;
            if (nuovaQuantita < 0) {
                logger.log(Level.WARNING, "Quantità insufficiente per libro ID: " + idLibro +
                        ". Disponibile: " + quantitaAttuale +
                        ", Richiesta: " + (-quantitaDaAggiornare));
                return false;
            }

            updateStmt.setInt(1, nuovaQuantita);
            updateStmt.setInt(2, idLibro);
            return updateStmt.executeUpdate() != 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore aggiornamento disponibilità libro ID: " + idLibro, e);
            throw e;
        }
    }
}
