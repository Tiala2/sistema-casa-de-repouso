package projeto.edu.unichristus.java.dao;

import projeto.edu.unichristus.java.model.Idosa;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IdosaDAOMySQL {


    public boolean salvar(Idosa idosa) {
        if (idosa == null) {
            return false;
        }
        String sql = "INSERT INTO idosa (nome, cpf, data_nascimento, nome_mae, cartao_sus, data_entrada) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, idosa.getNome());
            stmt.setString(2, idosa.getCpf());
            stmt.setDate(3, idosa.getDataNascimento() != null ? Date.valueOf(idosa.getDataNascimento()) : null);
            stmt.setString(4, idosa.getNomeMae());
            stmt.setString(5, idosa.getCartaoSUS());
            stmt.setDate(6, idosa.getDataEntrada() != null ? Date.valueOf(idosa.getDataEntrada()) : null);
            boolean saved = stmt.executeUpdate() > 0;
            Integer id = DatabaseConnection.generatedId(stmt);
            if (id != null) {
                idosa.setId(id);
            }
            return saved;
        } catch (SQLException e) {
            DaoErrors.log("Erro de persistencia", e);
            return false;
        }
    }

    public List<Idosa> listarTodos() {
        List<Idosa> lista = new ArrayList<>();
        String sql = "SELECT * FROM idosa ORDER BY nome, id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Idosa idosa = new Idosa(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("cpf"),
                    rs.getDate("data_nascimento") != null ? rs.getDate("data_nascimento").toLocalDate() : null,
                    rs.getString("nome_mae"),
                    rs.getString("cartao_sus"),
                    rs.getDate("data_entrada") != null ? rs.getDate("data_entrada").toLocalDate() : null
                );
                lista.add(idosa);
            }
        } catch (SQLException e) {
            DaoErrors.log("Erro de persistencia", e);
            return null;
        }
        return lista;
    }

    public Idosa buscarPorId(int id) {
        String sql = "SELECT * FROM idosa WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Idosa(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getDate("data_nascimento") != null ? rs.getDate("data_nascimento").toLocalDate() : null,
                        rs.getString("nome_mae"),
                        rs.getString("cartao_sus"),
                        rs.getDate("data_entrada") != null ? rs.getDate("data_entrada").toLocalDate() : null
                    );
                }
            }
        } catch (SQLException e) {
            DaoErrors.log("Erro de persistencia", e);
        }
        return null;
    }

    public boolean atualizar(Idosa idosa) {
        if (idosa == null) {
            return false;
        }
        String sql = "UPDATE idosa SET nome = ?, cpf = ?, data_nascimento = ?, nome_mae = ?, cartao_sus = ?, data_entrada = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idosa.getNome());
            stmt.setString(2, idosa.getCpf());
            stmt.setDate(3, idosa.getDataNascimento() != null ? Date.valueOf(idosa.getDataNascimento()) : null);
            stmt.setString(4, idosa.getNomeMae());
            stmt.setString(5, idosa.getCartaoSUS());
            stmt.setDate(6, idosa.getDataEntrada() != null ? Date.valueOf(idosa.getDataEntrada()) : null);
            stmt.setInt(7, idosa.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            DaoErrors.log("Erro de persistencia", e);
            return false;
        }
    }

    public boolean remover(int id) {
        String sql = "DELETE FROM idosa WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            DaoErrors.log("Erro de persistencia", e);
            return false;
        }
    }
}
