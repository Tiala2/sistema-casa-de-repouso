package projeto.edu.unichristus.java.controller;

import projeto.edu.unichristus.java.dao.EventoSentinelaDAOMySQL;
import projeto.edu.unichristus.java.dao.RelatorioDAOMySQL;
import projeto.edu.unichristus.java.model.Relatorio;
import projeto.edu.unichristus.java.model.TipoEventoSentinela;
import java.util.List;
import java.util.Map;

public class RelatorioController {
    private final RelatorioDAOMySQL relatorioDAO;

    public RelatorioController() {
        this.relatorioDAO = new RelatorioDAOMySQL();
    }

    public boolean adicionarRelatorio(Relatorio relatorio, int prontuarioId) {
        try {
            return relatorioDAO.salvar(relatorio, prontuarioId);
        } catch (Exception e) {
            ControllerErrors.log("Adicionar relatorio", e);
            return false;
        }
    }

    public List<Relatorio> listarRelatorios() {
        try {
            return relatorioDAO.listarTodos();
        } catch (Exception e) {
            ControllerErrors.log("Listar relatorios", e);
            return null;
        }
    }

    public Relatorio buscarPorId(int id) {
        try {
            return relatorioDAO.buscarPorId(id);
        } catch (Exception e) {
            ControllerErrors.log("Buscar relatorio", e);
            return null;
        }
    }

    public boolean atualizarRelatorio(Relatorio relatorio) {
        try {
            return relatorioDAO.atualizar(relatorio);
        } catch (Exception e) {
            ControllerErrors.log("Atualizar relatorio", e);
            return false;
        }
    }

    public boolean removerRelatorio(int id) {
        try {
            return relatorioDAO.remover(id);
        } catch (Exception e) {
            ControllerErrors.log("Remover relatorio", e);
            return false;
        }
    }

    public Map<TipoEventoSentinela, Double> percentualIdosasPorEvento(List<Integer> idsIdosas, int mes, int ano, EventoSentinelaDAOMySQL eventoDAO) {
        return relatorioDAO.percentualIdosasPorEvento(idsIdosas, mes, ano, eventoDAO);
    }
}
