package projeto.edu.unichristus.java.dao;

import projeto.edu.unichristus.java.model.Vacina;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;
import java.util.Properties;

public class VacinaDAOMySQL {
    private String url;
    private String user;
    private String password;

    public VacinaDAOMySQL() {
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

    public void salvar(Vacina vacina, int prontuarioId) {
        String sql = "INSERT INTO vacina (nome, data_ocorrencia, prontuario_id) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, vacina.getNome());
            stmt.setDate(2, vacina.getDataOcorrencia() != null ? Date.valueOf(vacina.getDataOcorrencia()) : null);
            stmt.setInt(3, prontuarioId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Vacina> listarTodos() {
        List<Vacina> lista = new ArrayList<>();
        String sql = "SELECT * FROM vacina";
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return null;
    }

    public boolean remover(int id) {
        String sql = "DELETE FROM vacina WHERE id = ?";
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
