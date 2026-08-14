package projeto.edu.unichristus.java.dao;

import projeto.edu.unichristus.java.model.ProntuarioMedico;
import projeto.edu.unichristus.java.model.Idosa;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProntuarioMedicoDAOMySQL {


    public boolean salvar(ProntuarioMedico prontuario) {
        if (prontuario == null || prontuario.getIdosa() == null || !DaoValidations.positiveId(prontuario.getIdosa().getId())) {
            return false;
        }
        String sql = "INSERT INTO prontuario_medico (data_hora_idosa, idosa_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setTimestamp(1, prontuario.getDataHoraIdosa() != null ? Timestamp.valueOf(prontuario.getDataHoraIdosa()) : null);
            stmt.setInt(2, prontuario.getIdosa().getId());
            boolean saved = stmt.executeUpdate() > 0;
            Integer id = DatabaseConnection.generatedId(stmt);
            if (id != null) {
                prontuario.setId(id);
            }
            return saved;
        } catch (SQLException e) {
            DaoErrors.log("Erro de persistencia", e);
            return false;
        }
    }

    public List<ProntuarioMedico> listarTodos() {
        List<ProntuarioMedico> lista = new ArrayList<>();
        String sql = "SELECT p.*, i.nome, i.cpf FROM prontuario_medico p JOIN idosa i ON p.idosa_id = i.id ORDER BY p.data_hora_idosa DESC, p.id DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Idosa idosa = new Idosa();
                idosa.setId(rs.getInt("idosa_id"));
                idosa.setNome(rs.getString("nome"));
                idosa.setCpf(rs.getString("cpf"));
                ProntuarioMedico prontuario = new ProntuarioMedico();
                prontuario.setId(rs.getInt("id"));
                Timestamp ts = rs.getTimestamp("data_hora_idosa");
                prontuario.setDataHoraIdosa(ts != null ? ts.toLocalDateTime() : null);
                prontuario.setIdosa(idosa);
                lista.add(prontuario);
            }
        } catch (SQLException e) {
            DaoErrors.log("Erro de persistencia", e);
            return null;
        }
        return lista;
    }

    public ProntuarioMedico buscarPorId(int id) {
        if (!DaoValidations.positiveId(id)) {
            return null;
        }
        String sql = "SELECT p.*, i.nome, i.cpf FROM prontuario_medico p JOIN idosa i ON p.idosa_id = i.id WHERE p.id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Idosa idosa = new Idosa();
                    idosa.setId(rs.getInt("idosa_id"));
                    idosa.setNome(rs.getString("nome"));
                    idosa.setCpf(rs.getString("cpf"));
                    ProntuarioMedico prontuario = new ProntuarioMedico();
                    prontuario.setId(rs.getInt("id"));
                    Timestamp ts = rs.getTimestamp("data_hora_idosa");
                    prontuario.setDataHoraIdosa(ts != null ? ts.toLocalDateTime() : null);
                    prontuario.setIdosa(idosa);
                    return prontuario;
                }
            }
        } catch (SQLException e) {
            DaoErrors.log("Erro de persistencia", e);
        }
        return null;
    }

    public boolean atualizar(ProntuarioMedico prontuario) {
        if (prontuario == null || !DaoValidations.positiveId(prontuario.getId()) || prontuario.getIdosa() == null || !DaoValidations.positiveId(prontuario.getIdosa().getId())) {
            return false;
        }
        String sql = "UPDATE prontuario_medico SET data_hora_idosa = ?, idosa_id = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, prontuario.getDataHoraIdosa() != null ? Timestamp.valueOf(prontuario.getDataHoraIdosa()) : null);
            stmt.setInt(2, prontuario.getIdosa().getId());
            stmt.setInt(3, prontuario.getId());
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
        String sql = "DELETE FROM prontuario_medico WHERE id = ?";
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
