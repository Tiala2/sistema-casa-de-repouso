package projeto.edu.unichristus.java.controller;

import projeto.edu.unichristus.java.model.ProfissionalSaude;
import projeto.edu.unichristus.java.dao.ProfissionalSaudeDAOMySQL;
import java.util.List;

public class ProfissionalSaudeController {
    private ProfissionalSaudeDAOMySQL profissionalDAO;

    public ProfissionalSaudeController() {
        this.profissionalDAO = new ProfissionalSaudeDAOMySQL();
    }

    public void adicionarProfissional(ProfissionalSaude profissional) {
        try {
            profissionalDAO.salvar(profissional);
        } catch (Exception e) {
            System.err.println("Erro ao adicionar profissional: " + e.getMessage());
        }
    }

    public List<ProfissionalSaude> listarProfissionais() {
        try {
            return profissionalDAO.listarTodos();
        } catch (Exception e) {
            System.err.println("Erro ao listar profissionais: " + e.getMessage());
            return null;
        }
    }

    public ProfissionalSaude buscarPorId(int id) {
        try {
            return profissionalDAO.buscarPorId(id);
        } catch (Exception e) {
            System.err.println("Erro ao buscar profissional: " + e.getMessage());
            return null;
        }
    }

    public boolean atualizarProfissional(ProfissionalSaude profissional) {
        try {
            return profissionalDAO.atualizar(profissional);
        } catch (Exception e) {
            System.err.println("Erro ao atualizar profissional: " + e.getMessage());
            return false;
        }
    }

    public boolean removerProfissional(int id) {
        try {
            return profissionalDAO.remover(id);
        } catch (Exception e) {
            System.err.println("Erro ao remover profissional: " + e.getMessage());
            return false;
        }
    }
}
