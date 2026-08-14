package projeto.edu.unichristus.java.dao;

import projeto.edu.unichristus.java.model.Prescricao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrescricaoDAOMySQL {


    public boolean salvar(Prescricao prescricao, int prontuarioId) {
        if (prescricao == null || !DaoValidations.positiveId(prontuarioId) || !DaoValidations.hasText(prescricao.getMedicamento())) {
            return false;
        }
        String sql = "INSERT INTO prescricao (medicamento, posologia, duracao, observacoes, prontuario_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, prescricao.getMedicamento());
            stmt.setString(2, prescricao.getPosologia());
            stmt.setString(3, prescricao.getDuracao());
            stmt.setString(4, prescricao.getObservacoes());
            stmt.setInt(5, prontuarioId);
            boolean saved = stmt.executeUpdate() > 0;
            Integer id = DatabaseConnection.generatedId(stmt);
            if (id != null) {
                prescricao.setId(id);
            }
            return saved;
        } catch (SQLException e) {
            DaoErrors.log("Erro de persistencia", e);
            return false;
        }
    }

    public List<Prescricao> listarTodos() {
        List<Prescricao> lista = new ArrayList<>();
        String sql = "SELECT * FROM prescricao ORDER BY id DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Prescricao prescricao = new Prescricao(
                    rs.getInt("id"),
                    rs.getString("medicamento"),
                    rs.getString("posologia"),
                    rs.getString("duracao"),
                    rs.getString("observacoes")
                );
                lista.add(prescricao);
            }
        } catch (SQLException e) {
            DaoErrors.log("Erro de persistencia", e);
            return null;
        }
        return lista;
    }

    public Prescricao buscarPorId(int id) {
        if (!DaoValidations.positiveId(id)) {
            return null;
        }
        String sql = "SELECT * FROM prescricao WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Prescricao(
                        rs.getInt("id"),
                        rs.getString("medicamento"),
                        rs.getString("posologia"),
                        rs.getString("duracao"),
                        rs.getString("observacoes")
                    );
                }
            }
        } catch (SQLException e) {
            DaoErrors.log("Erro de persistencia", e);
        }
        return null;
    }

    public boolean atualizar(Prescricao prescricao) {
        if (prescricao == null || !DaoValidations.positiveId(prescricao.getId()) || !DaoValidations.hasText(prescricao.getMedicamento())) {
            return false;
        }
        String sql = "UPDATE prescricao SET medicamento = ?, posologia = ?, duracao = ?, observacoes = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, prescricao.getMedicamento());
            stmt.setString(2, prescricao.getPosologia());
            stmt.setString(3, prescricao.getDuracao());
            stmt.setString(4, prescricao.getObservacoes());
            stmt.setInt(5, prescricao.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            DaoErrors.log("Erro de persistencia", e);
            return false;
        }
    }

    public boolean remover(int id) {
        if (!DaoValidations.positiveId(id)) {
            return false;
        }
        String sql = "DELETE FROM prescricao WHERE id = ?";
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
