package projeto.edu.unichristus.java.controller;

import projeto.edu.unichristus.java.model.Idosa;
import projeto.edu.unichristus.java.dao.IdosaDAOMySQL;
import java.util.List;

public class IdosaController {
    private IdosaDAOMySQL idosaDAO;

    public IdosaController() {
        this.idosaDAO = new IdosaDAOMySQL();
    }

    public boolean adicionarIdosa(Idosa idosa) {
        try {
            return idosaDAO.salvar(idosa);
        } catch (Exception e) {
            System.err.println("Erro ao adicionar idosa: " + e.getMessage());
            return false;
        }
    }

    public List<Idosa> listarIdosas() {
        try {
            return idosaDAO.listarTodos();
        } catch (Exception e) {
            System.err.println("Erro ao listar idosas: " + e.getMessage());
            return null;
        }
    }

    public Idosa buscarPorId(int id) {
        try {
            return idosaDAO.buscarPorId(id);
        } catch (Exception e) {
            System.err.println("Erro ao buscar idosa: " + e.getMessage());
            return null;
        }
    }

    public boolean atualizarIdosa(Idosa idosa) {
        try {
            return idosaDAO.atualizar(idosa);
        } catch (Exception e) {
            System.err.println("Erro ao atualizar idosa: " + e.getMessage());
            return false;
        }
    }

    public boolean removerIdosa(int id) {
        try {
            return idosaDAO.remover(id);
        } catch (Exception e) {
            System.err.println("Erro ao remover idosa: " + e.getMessage());
            return false;
        }
    }
}
