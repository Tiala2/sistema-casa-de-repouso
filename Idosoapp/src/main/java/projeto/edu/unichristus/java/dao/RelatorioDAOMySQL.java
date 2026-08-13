package projeto.edu.unichristus.java.dao;

import projeto.edu.unichristus.java.model.Relatorio;
import projeto.edu.unichristus.java.model.TipoEventoSentinela;
import projeto.edu.unichristus.java.model.EventoSentinela;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RelatorioDAOMySQL {


    public boolean salvar(Relatorio relatorio, int prontuarioId) {
        String sql = "INSERT INTO relatorio (descricao, tipo, prontuario_id) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, relatorio.getDescricao());
            stmt.setString(2, relatorio.getTipo());
            stmt.setInt(3, prontuarioId);
            boolean saved = stmt.executeUpdate() > 0;
            Integer id = DatabaseConnection.generatedId(stmt);
            if (id != null) {
                relatorio.setId(id);
            }
            return saved;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Relatorio> listarTodos() {
        List<Relatorio> lista = new ArrayList<>();
        String sql = "SELECT * FROM relatorio ORDER BY id DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Relatorio relatorio = new Relatorio(
                    rs.getInt("id"),
                    rs.getString("descricao"),
                    rs.getString("tipo")
                );
                lista.add(relatorio);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return lista;
    }

    public Relatorio buscarPorId(int id) {
        String sql = "SELECT * FROM relatorio WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Relatorio(
                        rs.getInt("id"),
                        rs.getString("descricao"),
                        rs.getString("tipo")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean atualizar(Relatorio relatorio) {
        String sql = "UPDATE relatorio SET descricao = ?, tipo = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, relatorio.getDescricao());
            stmt.setString(2, relatorio.getTipo());
            stmt.setInt(3, relatorio.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean remover(int id) {
        String sql = "DELETE FROM relatorio WHERE id = ?";
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

    public Map<TipoEventoSentinela, Double> percentualIdosasPorEvento(List<Integer> idsIdosas, int mes, int ano, EventoSentinelaDAOMySQL eventoDAO) {
        Map<TipoEventoSentinela, Double> percentualPorTipo = new java.util.EnumMap<>(TipoEventoSentinela.class);
        if (idsIdosas == null) {
            return percentualPorTipo;
        }
        int totalIdosas = idsIdosas.size();
        for (TipoEventoSentinela tipo : TipoEventoSentinela.values()) {
            int count = 0;
            for (Integer prontuarioId : idsIdosas) {
                List<EventoSentinela> eventos = eventoDAO.listarPorIdosaEPeriodo(prontuarioId, mes, ano);
                if (eventos == null) {
                    throw new IllegalStateException("Nao foi possivel consultar eventos sentinela para o relatorio.");
                }
                boolean idosaTeveEvento = false;
                for (EventoSentinela evento : eventos) {
                    if (tipo.equals(evento.getTipo())) {
                        idosaTeveEvento = true;
                        break;
                    }
                }
                if (idosaTeveEvento) count++;
            }
            percentualPorTipo.put(tipo, totalIdosas > 0 ? (count * 100.0) / totalIdosas : 0.0);
        }
        return percentualPorTipo;
    }
}
