package projeto.edu.unichristus.java.controller;

import projeto.edu.unichristus.java.model.Vacina;
import projeto.edu.unichristus.java.dao.VacinaDAOMySQL;
import java.util.List;

public class VacinaController {
    private VacinaDAOMySQL vacinaDAO;

    public VacinaController() {
        this.vacinaDAO = new VacinaDAOMySQL();
    }

    public void adicionarVacina(Vacina vacina, int prontuarioId) {
        try {
            vacinaDAO.salvar(vacina, prontuarioId);
        } catch (Exception e) {
            System.err.println("Erro ao adicionar vacina: " + e.getMessage());
        }
    }

    public List<Vacina> listarVacinas() {
        try {
            return vacinaDAO.listarTodos();
        } catch (Exception e) {
            System.err.println("Erro ao listar vacinas: " + e.getMessage());
            return null;
        }
    }

    public Vacina buscarPorId(int id) {
        try {
            return vacinaDAO.buscarPorId(id);
        } catch (Exception e) {
            System.err.println("Erro ao buscar vacina: " + e.getMessage());
            return null;
        }
    }

    public boolean atualizarVacina(Vacina vacina) {
        try {
            return vacinaDAO.atualizar(vacina);
        } catch (Exception e) {
            System.err.println("Erro ao atualizar vacina: " + e.getMessage());
            return false;
        }
    }

    public boolean removerVacina(int id) {
        try {
            return vacinaDAO.remover(id);
        } catch (Exception e) {
            System.err.println("Erro ao remover vacina: " + e.getMessage());
            return false;
        }
    }
}
