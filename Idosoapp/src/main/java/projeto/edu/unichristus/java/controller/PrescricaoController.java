package projeto.edu.unichristus.java.controller;

import projeto.edu.unichristus.java.dao.PrescricaoDAOMySQL;
import projeto.edu.unichristus.java.model.Prescricao;
import java.util.List;

public class PrescricaoController {
    private final PrescricaoDAOMySQL prescricaoDAO;

    public PrescricaoController() {
        this.prescricaoDAO = new PrescricaoDAOMySQL();
    }

    public boolean adicionarPrescricao(Prescricao prescricao, int prontuarioId) {
        try {
            return prescricaoDAO.salvar(prescricao, prontuarioId);
        } catch (Exception e) {
            ControllerErrors.log("Adicionar prescricao", e);
            return false;
        }
    }

    public List<Prescricao> listarPrescricoes() {
        try {
            return ControllerErrors.listOrEmpty(prescricaoDAO.listarTodos());
        } catch (Exception e) {
            return ControllerErrors.emptyList("Listar prescricoes", e);
        }
    }

    public Prescricao buscarPorId(int id) {
        try {
            return prescricaoDAO.buscarPorId(id);
        } catch (Exception e) {
            ControllerErrors.log("Buscar prescricao", e);
            return null;
        }
    }

    public boolean atualizarPrescricao(Prescricao prescricao) {
        try {
            return prescricaoDAO.atualizar(prescricao);
        } catch (Exception e) {
            ControllerErrors.log("Atualizar prescricao", e);
            return false;
        }
    }

    public boolean removerPrescricao(int id) {
        try {
            return prescricaoDAO.remover(id);
        } catch (Exception e) {
            ControllerErrors.log("Remover prescricao", e);
            return false;
        }
    }
}
