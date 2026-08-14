package projeto.edu.unichristus.java.controller;

import projeto.edu.unichristus.java.model.ProfissionalSaude;
import projeto.edu.unichristus.java.dao.ProfissionalSaudeDAOMySQL;
import java.util.List;

public class ProfissionalSaudeController {
    private final ProfissionalSaudeDAOMySQL profissionalDAO;

    public ProfissionalSaudeController() {
        this.profissionalDAO = new ProfissionalSaudeDAOMySQL();
    }

    public boolean adicionarProfissional(ProfissionalSaude profissional) {
        try {
            return profissionalDAO.salvar(profissional);
        } catch (Exception e) {
            ControllerErrors.log("Adicionar profissional", e);
            return false;
        }
    }

    public List<ProfissionalSaude> listarProfissionais() {
        try {
            return ControllerErrors.listOrEmpty(profissionalDAO.listarTodos());
        } catch (Exception e) {
            return ControllerErrors.emptyList("Listar profissionais", e);
        }
    }

    public ProfissionalSaude buscarPorId(int id) {
        try {
            return profissionalDAO.buscarPorId(id);
        } catch (Exception e) {
            ControllerErrors.log("Buscar profissional", e);
            return null;
        }
    }

    public boolean atualizarProfissional(ProfissionalSaude profissional) {
        try {
            return profissionalDAO.atualizar(profissional);
        } catch (Exception e) {
            ControllerErrors.log("Atualizar profissional", e);
            return false;
        }
    }

    public boolean removerProfissional(int id) {
        try {
            return profissionalDAO.remover(id);
        } catch (Exception e) {
            ControllerErrors.log("Remover profissional", e);
            return false;
        }
    }
}
