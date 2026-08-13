package projeto.edu.unichristus.java.controller;

import projeto.edu.unichristus.java.dao.EventoSentinelaDAOMySQL;
import projeto.edu.unichristus.java.model.EventoSentinela;
import projeto.edu.unichristus.java.model.TipoEventoSentinela;
import java.util.List;

public class EventoSentinelaController {
    private final EventoSentinelaDAOMySQL eventoDAO;

    public EventoSentinelaController() {
        this.eventoDAO = new EventoSentinelaDAOMySQL();
    }

    public boolean adicionarEvento(EventoSentinela evento, int prontuarioId) {
        try {
            return eventoDAO.salvar(evento, prontuarioId);
        } catch (Exception e) {
            ControllerErrors.log("Adicionar evento sentinela", e);
            return false;
        }
    }

    public List<EventoSentinela> listarEventos() {
        try {
            return eventoDAO.listarTodos();
        } catch (Exception e) {
            ControllerErrors.log("Listar eventos sentinela", e);
            return null;
        }
    }

    public EventoSentinela buscarPorId(int id) {
        try {
            return eventoDAO.buscarPorId(id);
        } catch (Exception e) {
            ControllerErrors.log("Buscar evento sentinela", e);
            return null;
        }
    }

    public boolean atualizarEvento(EventoSentinela evento) {
        try {
            return eventoDAO.atualizar(evento);
        } catch (Exception e) {
            ControllerErrors.log("Atualizar evento sentinela", e);
            return false;
        }
    }

    public boolean removerEvento(int id) {
        try {
            return eventoDAO.remover(id);
        } catch (Exception e) {
            ControllerErrors.log("Remover evento sentinela", e);
            return false;
        }
    }

    public List<EventoSentinela> listarPorIdosaEPeriodo(int prontuarioId, int mes, int ano) {
        try {
            return eventoDAO.listarPorIdosaEPeriodo(prontuarioId, mes, ano);
        } catch (Exception e) {
            ControllerErrors.log("Listar eventos sentinela por idosa e periodo", e);
            return null;
        }
    }

    public List<EventoSentinela> listarPorTipoEPeriodo(TipoEventoSentinela tipo, int mes, int ano) {
        try {
            return eventoDAO.listarPorTipoEPeriodo(tipo, mes, ano);
        } catch (Exception e) {
            ControllerErrors.log("Listar eventos sentinela por tipo e periodo", e);
            return null;
        }
    }
}
