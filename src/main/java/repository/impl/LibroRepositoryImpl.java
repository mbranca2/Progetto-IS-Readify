package repository.impl;

import model.Libro;
import repository.LibroRepository;
import utils.DBManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LibroRepositoryImpl implements LibroRepository {

    @Override
    public Optional<Libro> findById(Integer id) {
        String query = "SELECT * FROM Libro WHERE id = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToLibro(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Libro> findAll() {
        List<Libro> libri = new ArrayList<>();
        String query = "SELECT * FROM Libro";

        try (Connection conn = DBManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                libri.add(mapResultSetToLibro(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return libri;
    }

    @Override
    public Libro save(Libro libro) {
        String query = "INSERT INTO Libro (titolo, autore, prezzo, isbn, descrizione, disponibilita, copertina, categoria_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            setLibroParameters(stmt, libro);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating libro failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    libro.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating libro failed, no ID obtained.");
                }
            }
            return libro;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void update(Libro libro) {
        String query = "UPDATE Libro SET titolo=?, autore=?, prezzo=?, isbn=?, " +
                "descrizione=?, disponibilita=?, copertina=?, categoria_id=? WHERE id=?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            setLibroParameters(stmt, libro);
            stmt.setInt(9, libro.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer id) {
        String query = "DELETE FROM Libro WHERE id = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Libro> findByTitolo(String titolo) {
        List<Libro> libri = new ArrayList<>();
        String query = "SELECT * FROM Libro WHERE titolo LIKE ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + titolo + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                libri.add(mapResultSetToLibro(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return libri;
    }

    @Override
    public List<Libro> findByAutore(String autore) {
        List<Libro> libri = new ArrayList<>();
        String query = "SELECT * FROM Libro WHERE autore LIKE ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + autore + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                libri.add(mapResultSetToLibro(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return libri;
    }

    @Override
    public List<Libro> findByCategoria(int categoriaId) {
        List<Libro> libri = new ArrayList<>();
        String query = "SELECT * FROM Libro WHERE categoria_id = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, categoriaId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                libri.add(mapResultSetToLibro(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return libri;
    }

    @Override
    public boolean updateDisponibilita(int libroId, int quantita) {
        String query = "UPDATE Libro SET disponibilita = disponibilita + ? WHERE id = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, quantita);
            stmt.setInt(2, libroId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Libro mapResultSetToLibro(ResultSet rs) throws SQLException {
        Libro libro = new Libro();
        libro.setId(rs.getInt("id"));
        libro.setTitolo(rs.getString("titolo"));
        libro.setAutore(rs.getString("autore"));
        libro.setPrezzo(rs.getBigDecimal("prezzo"));
        libro.setIsbn(rs.getString("isbn"));
        libro.setDescrizione(rs.getString("descrizione"));
        libro.setDisponibilita(rs.getInt("disponibilita"));
        libro.setCopertina(rs.getString("copertina"));
        // Assuming there's a categoriaId field in Libro class
        // libro.setCategoriaId(rs.getInt("categoria_id"));
        return libro;
    }

    private void setLibroParameters(PreparedStatement stmt, Libro libro) throws SQLException {
        stmt.setString(1, libro.getTitolo());
        stmt.setString(2, libro.getAutore());
        stmt.setBigDecimal(3, libro.getPrezzo());
        stmt.setString(4, libro.getIsbn());
        stmt.setString(5, libro.getDescrizione());
        stmt.setInt(6, libro.getDisponibilita());
        stmt.setString(7, libro.getCopertina());
        // stmt.setInt(8, libro.getCategoriaId());
    }
}