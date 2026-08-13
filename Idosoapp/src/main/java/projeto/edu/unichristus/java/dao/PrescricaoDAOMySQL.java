package projeto.edu.unichristus.java.dao;

import projeto.edu.unichristus.java.model.Prescricao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrescricaoDAOMySQL {


    public boolean salvar(Prescricao prescricao, int prontuarioId) {
        String sql = "INSERT INTO prescricao (medicamento, posologia, duracao, observacoes, prontuario_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, prescricao.getMedicamento());
            stmt.setString(2, prescricao.getPosologia());
            stmt.setString(3, prescricao.getDuracao());
            stmt.setString(4, prescricao.getObservacoes());
            stmt.setInt(5, prontuarioId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Prescricao> listarTodos() {
        List<Prescricao> lista = new ArrayList<>();
        String sql = "SELECT * FROM prescricao";
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
            e.printStackTrace();
        }
        return lista;
    }

    public Prescricao buscarPorId(int id) {
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
            e.printStackTrace();
        }
        return null;
    }

    public boolean atualizar(Prescricao prescricao) {
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
            e.printStackTrace();
            return false;
        }
    }

    public boolean remover(int id) {
        String sql = "DELETE FROM prescricao WHERE id = ?";
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
