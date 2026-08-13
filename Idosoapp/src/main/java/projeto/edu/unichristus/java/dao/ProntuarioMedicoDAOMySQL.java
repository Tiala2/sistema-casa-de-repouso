package projeto.edu.unichristus.java.dao;

import projeto.edu.unichristus.java.model.ProntuarioMedico;
import projeto.edu.unichristus.java.model.Idosa;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;
import java.util.Properties;

public class ProntuarioMedicoDAOMySQL {
    private String url;
    private String user;
    private String password;

    public ProntuarioMedicoDAOMySQL() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            url = prop.getProperty("db.url");
            user = prop.getProperty("db.user");
            password = prop.getProperty("db.password");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void salvar(ProntuarioMedico prontuario) {
        String sql = "INSERT INTO prontuario_medico (data_hora_idosa, idosa_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, prontuario.getDataHoraIdosa() != null ? Timestamp.valueOf(prontuario.getDataHoraIdosa()) : null);
            stmt.setInt(2, prontuario.getIdosa().getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ProntuarioMedico> listarTodos() {
        List<ProntuarioMedico> lista = new ArrayList<>();
        String sql = "SELECT p.*, i.nome, i.cpf FROM prontuario_medico p JOIN idosa i ON p.idosa_id = i.id";
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
            e.printStackTrace();
        }
        return lista;
    }

    public ProntuarioMedico buscarPorId(int id) {
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
            e.printStackTrace();
        }
        return null;
    }

    public boolean remover(int id) {
        String sql = "DELETE FROM prontuario_medico WHERE id = ?";
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
