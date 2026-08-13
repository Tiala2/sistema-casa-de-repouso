package projeto.edu.unichristus.java.controller;

import projeto.edu.unichristus.java.model.EventoSentinela;
import projeto.edu.unichristus.java.dao.EventoSentinelaDAOMySQL;
import projeto.edu.unichristus.java.model.TipoEventoSentinela;
import java.util.List;

public class EventoSentinelaController {
    private EventoSentinelaDAOMySQL eventoDAO;

    public EventoSentinelaController() {
        this.eventoDAO = new EventoSentinelaDAOMySQL();
    }

    public void adicionarEvento(EventoSentinela evento, int prontuarioId) {
        try {
            eventoDAO.salvar(evento, prontuarioId);
        } catch (Exception e) {
            System.err.println("Erro ao adicionar evento sentinela: " + e.getMessage());
        }
    }

    public List<EventoSentinela> listarEventos() {
        try {
            return eventoDAO.listarTodos();
        } catch (Exception e) {
            System.err.println("Erro ao listar eventos sentinela: " + e.getMessage());
            return null;
        }
    }

    public EventoSentinela buscarPorId(int id) {
        try {
            return eventoDAO.buscarPorId(id);
        } catch (Exception e) {
            System.err.println("Erro ao buscar evento sentinela: " + e.getMessage());
            return null;
        }
    }

    public boolean atualizarEvento(EventoSentinela evento) {
        try {
            return eventoDAO.atualizar(evento);
        } catch (Exception e) {
            System.err.println("Erro ao atualizar evento sentinela: " + e.getMessage());
            return false;
        }
    }

    public boolean removerEvento(int id) {
        try {
            return eventoDAO.remover(id);
        } catch (Exception e) {
            System.err.println("Erro ao remover evento sentinela: " + e.getMessage());
            return false;
        }
    }

    public List<EventoSentinela> listarPorIdosaEPeriodo(int prontuarioId, int mes, int ano) {
        try {
            return eventoDAO.listarPorIdosaEPeriodo(prontuarioId, mes, ano);
        } catch (Exception e) {
            System.err.println("Erro ao listar eventos sentinela por idosa e período: " + e.getMessage());
            return null;
        }
    }

    public List<EventoSentinela> listarPorTipoEPeriodo(TipoEventoSentinela tipo, int mes, int ano) {
        try {
            return eventoDAO.listarPorTipoEPeriodo(tipo, mes, ano);
        } catch (Exception e) {
            System.err.println("Erro ao listar eventos sentinela por tipo e período: " + e.getMessage());
            return null;
        }
    }
}
