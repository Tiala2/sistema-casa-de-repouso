package projeto.edu.unichristus.java.dao;

import projeto.edu.unichristus.java.model.ProfissionalSaude;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfissionalSaudeDAOMySQL {


    public boolean salvar(ProfissionalSaude prof) {
        String sql = "INSERT INTO profissional_saude (nome, especialidade, registro_profissional) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, prof.getNome());
            stmt.setString(2, prof.getEspecialidade());
            stmt.setString(3, prof.getRegistroProfissional());
            boolean saved = stmt.executeUpdate() > 0;
            Integer id = DatabaseConnection.generatedId(stmt);
            if (id != null) {
                prof.setId(id);
            }
            return saved;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ProfissionalSaude> listarTodos() {
        List<ProfissionalSaude> lista = new ArrayList<>();
        String sql = "SELECT * FROM profissional_saude ORDER BY nome, id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ProfissionalSaude prof = new ProfissionalSaude(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("especialidade"),
                    rs.getString("registro_profissional")
                );
                lista.add(prof);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public ProfissionalSaude buscarPorId(int id) {
        String sql = "SELECT * FROM profissional_saude WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new ProfissionalSaude(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("especialidade"),
                        rs.getString("registro_profissional")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean atualizar(ProfissionalSaude prof) {
        String sql = "UPDATE profissional_saude SET nome = ?, especialidade = ?, registro_profissional = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, prof.getNome());
            stmt.setString(2, prof.getEspecialidade());
            stmt.setString(3, prof.getRegistroProfissional());
            stmt.setInt(4, prof.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean remover(int id) {
        String sql = "DELETE FROM profissional_saude WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
