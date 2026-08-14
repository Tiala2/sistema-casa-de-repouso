package projeto.edu.unichristus.java.dao;

import projeto.edu.unichristus.java.model.EventoSentinela;
import projeto.edu.unichristus.java.model.TipoEventoSentinela;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EventoSentinelaDAOMySQL {


    public boolean salvar(EventoSentinela evento, int prontuarioId) {
        if (evento == null || evento.getTipo() == null || !DaoValidations.positiveId(prontuarioId) || evento.getDataOcorrencia() == null) {
            return false;
        }
        String sql = "INSERT INTO evento_sentinela (tipo, data_ocorrencia, prontuario_id) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, evento.getTipo().name());
            stmt.setDate(2, evento.getDataOcorrencia() != null ? Date.valueOf(evento.getDataOcorrencia()) : null);
            stmt.setInt(3, prontuarioId);
            boolean saved = stmt.executeUpdate() > 0;
            Integer id = DatabaseConnection.generatedId(stmt);
            if (id != null) {
                evento.setId(id);
            }
            return saved;
        } catch (SQLException e) {
            DaoErrors.log("Erro de persistencia", e);
            return false;
        }
    }

    public List<EventoSentinela> listarTodos() {
        List<EventoSentinela> lista = new ArrayList<>();
        String sql = "SELECT * FROM evento_sentinela ORDER BY data_ocorrencia DESC, id DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                EventoSentinela evento = new EventoSentinela(
                    rs.getInt("id"),
                    TipoEventoSentinela.valueOf(rs.getString("tipo")),
                    rs.getDate("data_ocorrencia") != null ? rs.getDate("data_ocorrencia").toLocalDate() : null
                );
                lista.add(evento);
            }
        } catch (SQLException e) {
            DaoErrors.log("Erro de persistencia", e);
            return null;
        }
        return lista;
    }

    public EventoSentinela buscarPorId(int id) {
        if (!DaoValidations.positiveId(id)) {
            return null;
        }
        String sql = "SELECT * FROM evento_sentinela WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new EventoSentinela(
                        rs.getInt("id"),
                        TipoEventoSentinela.valueOf(rs.getString("tipo")),
                        rs.getDate("data_ocorrencia") != null ? rs.getDate("data_ocorrencia").toLocalDate() : null
                    );
                }
            }
        } catch (SQLException e) {
            DaoErrors.log("Erro de persistencia", e);
        }
        return null;
    }

    public boolean atualizar(EventoSentinela evento) {
        if (evento == null || !DaoValidations.positiveId(evento.getId()) || evento.getTipo() == null || evento.getDataOcorrencia() == null) {
            return false;
        }
        String sql = "UPDATE evento_sentinela SET tipo = ?, data_ocorrencia = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, evento.getTipo().name());
            stmt.setDate(2, evento.getDataOcorrencia() != null ? Date.valueOf(evento.getDataOcorrencia()) : null);
            stmt.setInt(3, evento.getId());
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
        String sql = "DELETE FROM evento_sentinela WHERE id = ?";
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

    public List<EventoSentinela> listarPorIdosaEPeriodo(int prontuarioId, int mes, int ano) {
        if (prontuarioId <= 0 || !periodoValido(mes, ano)) {
            return new ArrayList<>();
        }
        List<EventoSentinela> lista = new ArrayList<>();
        String sql = "SELECT * FROM evento_sentinela WHERE prontuario_id = ? AND data_ocorrencia >= ? AND data_ocorrencia < ? ORDER BY data_ocorrencia DESC, id DESC";
        LocalDate inicio = LocalDate.of(ano, mes, 1);
        LocalDate fim = inicio.plusMonths(1);
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, prontuarioId);
            stmt.setDate(2, Date.valueOf(inicio));
            stmt.setDate(3, Date.valueOf(fim));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    EventoSentinela evento = new EventoSentinela(
                        rs.getInt("id"),
                        TipoEventoSentinela.valueOf(rs.getString("tipo")),
                        rs.getDate("data_ocorrencia") != null ? rs.getDate("data_ocorrencia").toLocalDate() : null
                    );
                    lista.add(evento);
                }
            }
        } catch (SQLException e) {
            DaoErrors.log("Erro de persistencia", e);
            return null;
        }
        return lista;
    }

    public List<EventoSentinela> listarPorTipoEPeriodo(TipoEventoSentinela tipo, int mes, int ano) {
        if (tipo == null || !periodoValido(mes, ano)) {
            return new ArrayList<>();
        }
        List<EventoSentinela> lista = new ArrayList<>();
        String sql = "SELECT * FROM evento_sentinela WHERE tipo = ? AND data_ocorrencia >= ? AND data_ocorrencia < ? ORDER BY data_ocorrencia DESC, id DESC";
        LocalDate inicio = LocalDate.of(ano, mes, 1);
        LocalDate fim = inicio.plusMonths(1);
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tipo.name());
            stmt.setDate(2, Date.valueOf(inicio));
            stmt.setDate(3, Date.valueOf(fim));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    EventoSentinela evento = new EventoSentinela(
                        rs.getInt("id"),
                        tipo,
                        rs.getDate("data_ocorrencia") != null ? rs.getDate("data_ocorrencia").toLocalDate() : null
                    );
                    lista.add(evento);
                }
            }
        } catch (SQLException e) {
            DaoErrors.log("Erro de persistencia", e);
            return null;
        }
        return lista;
    }

    private boolean periodoValido(int mes, int ano) {
        return mes >= 1 && mes <= 12 && ano > 0;
    }
}
