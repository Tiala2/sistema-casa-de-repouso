package projeto.edu.unichristus.java.dao;

import projeto.edu.unichristus.java.model.Consulta;
import projeto.edu.unichristus.java.model.ProfissionalSaude;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConsultaDAOMySQL {


    public boolean salvar(Consulta consulta) {
        if (consulta == null || consulta.getDataHora() == null || consulta.getProfissional() == null || !DaoValidations.positiveId(consulta.getProfissional().getId())) {
            return false;
        }
        String sql = "INSERT INTO consulta (data_hora, profissional_id, tipo, motivo, diagnostico, prontuario_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setTimestamp(1, Timestamp.valueOf(consulta.getDataHora()));
            stmt.setInt(2, consulta.getProfissional().getId());
            stmt.setString(3, consulta.getTipo());
            stmt.setString(4, consulta.getMotivo());
            stmt.setString(5, consulta.getDiagnostico());
            stmt.setNull(6, Types.INTEGER); // prontuario_id
            boolean saved = stmt.executeUpdate() > 0;
            Integer id = DatabaseConnection.generatedId(stmt);
            if (id != null) {
                consulta.setId(id);
            }
            return saved;
        } catch (SQLException e) {
            DaoErrors.log("Erro de persistencia", e);
            return false;
        }
    }

    public List<Consulta> listarTodos() {
        List<Consulta> consultas = new ArrayList<>();
        String sql = "SELECT c.*, p.id as prof_id, p.nome, p.especialidade, p.registro_profissional FROM consulta c JOIN profissional_saude p ON c.profissional_id = p.id ORDER BY c.data_hora DESC, c.id DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ProfissionalSaude prof = new ProfissionalSaude(
                    rs.getInt("prof_id"),
                    rs.getString("nome"),
                    rs.getString("especialidade"),
                    rs.getString("registro_profissional")
                );
                Consulta consulta = new Consulta(
                    rs.getInt("id"),
                    toLocalDateTime(rs.getTimestamp("data_hora")),
                    prof,
                    rs.getString("tipo"),
                    rs.getString("motivo"),
                    rs.getString("diagnostico")
                );
                consultas.add(consulta);
            }
        } catch (SQLException e) {
            return DaoErrors.emptyList("Erro de persistencia", e);
        }
        return consultas;
    }

    public Consulta buscarPorId(int id) {
        if (!DaoValidations.positiveId(id)) {
            return null;
        }
        String sql = "SELECT c.*, p.id as prof_id, p.nome, p.especialidade, p.registro_profissional FROM consulta c JOIN profissional_saude p ON c.profissional_id = p.id WHERE c.id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ProfissionalSaude prof = new ProfissionalSaude(
                        rs.getInt("prof_id"),
                        rs.getString("nome"),
                        rs.getString("especialidade"),
                        rs.getString("registro_profissional")
                    );
                    return new Consulta(
                        rs.getInt("id"),
                        toLocalDateTime(rs.getTimestamp("data_hora")),
                        prof,
                        rs.getString("tipo"),
                        rs.getString("motivo"),
                        rs.getString("diagnostico")
                    );
                }
            }
        } catch (SQLException e) {
            DaoErrors.log("Erro de persistencia", e);
        }
        return null;
    }

    public boolean atualizar(Consulta consulta) {
        if (consulta == null || !DaoValidations.positiveId(consulta.getId()) || consulta.getDataHora() == null || consulta.getProfissional() == null || !DaoValidations.positiveId(consulta.getProfissional().getId())) {
            return false;
        }
        String sql = "UPDATE consulta SET data_hora = ?, profissional_id = ?, tipo = ?, motivo = ?, diagnostico = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(consulta.getDataHora()));
            stmt.setInt(2, consulta.getProfissional().getId());
            stmt.setString(3, consulta.getTipo());
            stmt.setString(4, consulta.getMotivo());
            stmt.setString(5, consulta.getDiagnostico());
            stmt.setInt(6, consulta.getId());
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
        String sql = "DELETE FROM consulta WHERE id = ?";
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

    private LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }
}
