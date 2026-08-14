package projeto.edu.unichristus.java.dao;

import projeto.edu.unichristus.java.model.Vacina;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VacinaDAOMySQL {


    public boolean salvar(Vacina vacina, int prontuarioId) {
        if (vacina == null || prontuarioId <= 0) {
            return false;
        }
        String sql = "INSERT INTO vacina (nome, data_ocorrencia, prontuario_id) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, vacina.getNome());
            stmt.setDate(2, vacina.getDataOcorrencia() != null ? Date.valueOf(vacina.getDataOcorrencia()) : null);
            stmt.setInt(3, prontuarioId);
            boolean saved = stmt.executeUpdate() > 0;
            Integer id = DatabaseConnection.generatedId(stmt);
            if (id != null) {
                vacina.setId(id);
            }
            return saved;
        } catch (SQLException e) {
            DaoErrors.log("Erro de persistencia", e);
            return false;
        }
    }

    public List<Vacina> listarTodos() {
        List<Vacina> lista = new ArrayList<>();
        String sql = "SELECT * FROM vacina ORDER BY data_ocorrencia DESC, id DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Vacina vacina = new Vacina(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getDate("data_ocorrencia") != null ? rs.getDate("data_ocorrencia").toLocalDate() : null
                );
                lista.add(vacina);
            }
        } catch (SQLException e) {
            DaoErrors.log("Erro de persistencia", e);
            return null;
        }
        return lista;
    }

    public Vacina buscarPorId(int id) {
        String sql = "SELECT * FROM vacina WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Vacina(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getDate("data_ocorrencia") != null ? rs.getDate("data_ocorrencia").toLocalDate() : null
                    );
                }
            }
        } catch (SQLException e) {
            DaoErrors.log("Erro de persistencia", e);
        }
        return null;
    }

    public boolean atualizar(Vacina vacina) {
        if (vacina == null) {
            return false;
        }
        String sql = "UPDATE vacina SET nome = ?, data_ocorrencia = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, vacina.getNome());
            stmt.setDate(2, vacina.getDataOcorrencia() != null ? Date.valueOf(vacina.getDataOcorrencia()) : null);
            stmt.setInt(3, vacina.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            DaoErrors.log("Erro de persistencia", e);
            return false;
        }
    }

    public boolean remover(int id) {
        String sql = "DELETE FROM vacina WHERE id = ?";
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
