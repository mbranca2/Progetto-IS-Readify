package repository.impl;

import model.Categoria;
import repository.CategoriaRepository;
import utils.DBManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoriaRepositoryImpl implements CategoriaRepository {

    @Override
    public List<Categoria> findAll() {
        List<Categoria> categorie = new ArrayList<>();
        String sql = "SELECT * FROM Categoria";

        try (Connection conn = DBManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                categorie.add(mapResultSetToCategoria(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categorie;
    }

    @Override
    public Optional<Categoria> findById(Integer id) {
        String sql = "SELECT * FROM Categoria WHERE id_categoria = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToCategoria(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Categoria save(Categoria categoria) {
        if (categoria.getIdCategoria() > 0) {
            return update(categoria);
        } else {
            return insert(categoria);
        }
    }

    private Categoria insert(Categoria categoria) {
        String sql = "INSERT INTO Categoria (nome, descrizione) VALUES (?, ?)";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, categoria.getNome());
            stmt.setString(2, categoria.getDescrizione());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        categoria.setIdCategoria(generatedKeys.getInt(1));
                        return categoria;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Categoria update(Categoria categoria) {
        String sql = "UPDATE Categoria SET nome = ?, descrizione = ? WHERE id_categoria = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categoria.getNome());
            stmt.setString(2, categoria.getDescrizione());
            stmt.setInt(3, categoria.getIdCategoria());

            if (stmt.executeUpdate() > 0) {
                return categoria;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM Categoria WHERE id_categoria = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Categoria> findByNome(String nome) {
        String sql = "SELECT * FROM Categoria WHERE nome = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToCategoria(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Categoria> findByLibroId(int idLibro) {
        List<Categoria> categorie = new ArrayList<>();
        String sql = "SELECT c.* FROM Categoria c " +
                "JOIN Appartiene a ON c.id_categoria = a.id_categoria " +
                "WHERE a.id_libro = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idLibro);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                categorie.add(mapResultSetToCategoria(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categorie;
    }

    private Categoria mapResultSetToCategoria(ResultSet rs) throws SQLException {
        Categoria categoria = new Categoria();
        categoria.setIdCategoria(rs.getInt("id_categoria"));
        categoria.setNome(rs.getString("nome"));
        categoria.setDescrizione(rs.getString("descrizione"));
        return categoria;
    }
}