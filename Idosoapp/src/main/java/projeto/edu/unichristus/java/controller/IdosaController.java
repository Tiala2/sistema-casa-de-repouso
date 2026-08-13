package projeto.edu.unichristus.java.controller;

import projeto.edu.unichristus.java.model.Idosa;
import projeto.edu.unichristus.java.dao.IdosaDAOMySQL;
import java.util.List;

public class IdosaController {
    private final IdosaDAOMySQL idosaDAO;

    public IdosaController() {
        this.idosaDAO = new IdosaDAOMySQL();
    }

    public boolean adicionarIdosa(Idosa idosa) {
        try {
            return idosaDAO.salvar(idosa);
        } catch (Exception e) {
            ControllerErrors.log("Adicionar idosa", e);
            return false;
        }
    }

    public List<Idosa> listarIdosas() {
        try {
            return idosaDAO.listarTodos();
        } catch (Exception e) {
            ControllerErrors.log("Listar idosas", e);
            return null;
        }
    }

    public Idosa buscarPorId(int id) {
        try {
            return idosaDAO.buscarPorId(id);
        } catch (Exception e) {
            ControllerErrors.log("Buscar idosa", e);
            return null;
        }
    }

    public boolean atualizarIdosa(Idosa idosa) {
        try {
            return idosaDAO.atualizar(idosa);
        } catch (Exception e) {
            ControllerErrors.log("Atualizar idosa", e);
            return false;
        }
    }

    public boolean removerIdosa(int id) {
        try {
            return idosaDAO.remover(id);
        } catch (Exception e) {
            ControllerErrors.log("Remover idosa", e);
            return false;
        }
    }
}
