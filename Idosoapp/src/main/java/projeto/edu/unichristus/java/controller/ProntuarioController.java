package projeto.edu.unichristus.java.controller;

import projeto.edu.unichristus.java.dao.ProntuarioMedicoDAOMySQL;
import projeto.edu.unichristus.java.model.ProntuarioMedico;
import java.util.List;

public class ProntuarioController {
    private final ProntuarioMedicoDAOMySQL prontuarioDAO;

    public ProntuarioController() {
        this.prontuarioDAO = new ProntuarioMedicoDAOMySQL();
    }

    public boolean adicionarProntuario(ProntuarioMedico prontuario) {
        try {
            return prontuarioDAO.salvar(prontuario);
        } catch (Exception e) {
            ControllerErrors.log("Adicionar prontuario", e);
            return false;
        }
    }

    public List<ProntuarioMedico> listarProntuarios() {
        try {
            return prontuarioDAO.listarTodos();
        } catch (Exception e) {
            return ControllerErrors.emptyList("Listar prontuarios", e);
        }
    }

    public ProntuarioMedico buscarPorId(int id) {
        try {
            return prontuarioDAO.buscarPorId(id);
        } catch (Exception e) {
            ControllerErrors.log("Buscar prontuario", e);
            return null;
        }
    }

    public boolean atualizarProntuario(ProntuarioMedico prontuario) {
        try {
            return prontuarioDAO.atualizar(prontuario);
        } catch (Exception e) {
            ControllerErrors.log("Atualizar prontuario", e);
            return false;
        }
    }

    public boolean removerProntuario(int id) {
        try {
            return prontuarioDAO.remover(id);
        } catch (Exception e) {
            ControllerErrors.log("Remover prontuario", e);
            return false;
        }
    }
}
