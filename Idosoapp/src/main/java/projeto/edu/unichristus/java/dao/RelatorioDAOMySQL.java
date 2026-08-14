package projeto.edu.unichristus.java.dao;

import projeto.edu.unichristus.java.model.Relatorio;
import projeto.edu.unichristus.java.model.TipoEventoSentinela;
import projeto.edu.unichristus.java.model.EventoSentinela;
import java.sql.*;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RelatorioDAOMySQL {


    public boolean salvar(Relatorio relatorio, int prontuarioId) {
        if (relatorio == null || prontuarioId <= 0) {
            return false;
        }
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
            DaoErrors.log("Erro de persistencia", e);
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
            DaoErrors.log("Erro de persistencia", e);
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
            DaoErrors.log("Erro de persistencia", e);
        }
        return null;
    }

    public boolean atualizar(Relatorio relatorio) {
        if (relatorio == null) {
            return false;
        }
        String sql = "UPDATE relatorio SET descricao = ?, tipo = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, relatorio.getDescricao());
            stmt.setString(2, relatorio.getTipo());
            stmt.setInt(3, relatorio.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            DaoErrors.log("Erro de persistencia", e);
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
            DaoErrors.log("Erro de persistencia", e);
            return false;
        }
    }

    public Map<TipoEventoSentinela, Double> percentualIdosasPorEvento(List<Integer> idsIdosas, int mes, int ano, EventoSentinelaDAOMySQL eventoDAO) {
        Map<TipoEventoSentinela, Double> percentualPorTipo = new EnumMap<>(TipoEventoSentinela.class);
        if (idsIdosas == null || eventoDAO == null) {
            return percentualPorTipo;
        }
        Map<TipoEventoSentinela, Integer> ocorrenciasPorTipo = new EnumMap<>(TipoEventoSentinela.class);
        int totalProntuariosValidos = 0;

        for (Integer prontuarioId : idsIdosas) {
            if (prontuarioId == null || prontuarioId <= 0) {
                continue;
            }
            totalProntuariosValidos++;
            List<EventoSentinela> eventos = eventoDAO.listarPorIdosaEPeriodo(prontuarioId, mes, ano);
            if (eventos == null) {
                throw new IllegalStateException("Nao foi possivel consultar eventos sentinela para o relatorio.");
            }
            Set<TipoEventoSentinela> tiposDaIdosa = EnumSet.noneOf(TipoEventoSentinela.class);
            for (EventoSentinela evento : eventos) {
                if (evento != null && evento.getTipo() != null) {
                    tiposDaIdosa.add(evento.getTipo());
                }
            }
            for (TipoEventoSentinela tipo : tiposDaIdosa) {
                Integer ocorrencias = ocorrenciasPorTipo.get(tipo);
                ocorrenciasPorTipo.put(tipo, ocorrencias == null ? 1 : ocorrencias + 1);
            }
        }

        for (TipoEventoSentinela tipo : TipoEventoSentinela.values()) {
            Integer ocorrencias = ocorrenciasPorTipo.get(tipo);
            percentualPorTipo.put(tipo, totalProntuariosValidos > 0 && ocorrencias != null ? (ocorrencias * 100.0) / totalProntuariosValidos : 0.0);
        }
        return percentualPorTipo;
    }
}
