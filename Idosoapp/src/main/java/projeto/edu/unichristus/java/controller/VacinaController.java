package projeto.edu.unichristus.java.controller;

import projeto.edu.unichristus.java.model.Vacina;
import projeto.edu.unichristus.java.dao.VacinaDAOMySQL;
import java.util.List;

public class VacinaController {
    private final VacinaDAOMySQL vacinaDAO;

    public VacinaController() {
        this.vacinaDAO = new VacinaDAOMySQL();
    }

    public boolean adicionarVacina(Vacina vacina, int prontuarioId) {
        try {
            return vacinaDAO.salvar(vacina, prontuarioId);
        } catch (Exception e) {
            ControllerErrors.log("Adicionar vacina", e);
            return false;
        }
    }

    public List<Vacina> listarVacinas() {
        try {
            return vacinaDAO.listarTodos();
        } catch (Exception e) {
            ControllerErrors.log("Listar vacinas", e);
            return null;
        }
    }

    public Vacina buscarPorId(int id) {
        try {
            return vacinaDAO.buscarPorId(id);
        } catch (Exception e) {
            ControllerErrors.log("Buscar vacina", e);
            return null;
        }
    }

    public boolean atualizarVacina(Vacina vacina) {
        try {
            return vacinaDAO.atualizar(vacina);
        } catch (Exception e) {
            ControllerErrors.log("Atualizar vacina", e);
            return false;
        }
    }

    public boolean removerVacina(int id) {
        try {
            return vacinaDAO.remover(id);
        } catch (Exception e) {
            ControllerErrors.log("Remover vacina", e);
            return false;
        }
    }
}
