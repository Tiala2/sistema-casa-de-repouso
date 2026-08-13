package projeto.edu.unichristus.java.dao;

import projeto.edu.unichristus.java.model.Consulta;
import projeto.edu.unichristus.java.model.ProfissionalSaude;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;
import java.util.Properties;

public class ConsultaDAOMySQL {
    private String url;
    private String user;
    private String password;

    public ConsultaDAOMySQL() {
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

    public void salvar(Consulta consulta) {
        String sql = "INSERT INTO consulta (data_hora, profissional_id, tipo, motivo, diagnostico, prontuario_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(consulta.getDataHora()));
            stmt.setInt(2, consulta.getProfissional().getId());
            stmt.setString(3, consulta.getTipo());
            stmt.setString(4, consulta.getMotivo());
            stmt.setString(5, consulta.getDiagnostico());
            stmt.setNull(6, Types.INTEGER); // prontuario_id
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Consulta> listarTodos() {
        List<Consulta> consultas = new ArrayList<>();
        String sql = "SELECT c.*, p.id as prof_id, p.nome, p.especialidade, p.registro_profissional FROM consulta c JOIN profissional_saude p ON c.profissional_id = p.id";
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
                    rs.getTimestamp("data_hora").toLocalDateTime(),
                    prof,
                    rs.getString("tipo"),
                    rs.getString("motivo"),
                    rs.getString("diagnostico")
                );
                consultas.add(consulta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return consultas;
    }

    public Consulta buscarPorId(int id) {
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
                        rs.getTimestamp("data_hora").toLocalDateTime(),
                        prof,
                        rs.getString("tipo"),
                        rs.getString("motivo"),
                        rs.getString("diagnostico")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean remover(int id) {
        String sql = "DELETE FROM consulta WHERE id = ?";
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
