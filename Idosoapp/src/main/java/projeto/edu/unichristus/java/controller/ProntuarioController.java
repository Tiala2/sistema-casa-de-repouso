package projeto.edu.unichristus.java.controller;

import projeto.edu.unichristus.java.model.ProntuarioMedico;
import projeto.edu.unichristus.java.dao.ProntuarioMedicoDAOMySQL;
import java.util.List;

public class ProntuarioController {
    private ProntuarioMedicoDAOMySQL prontuarioDAO;

    public ProntuarioController() {
        this.prontuarioDAO = new ProntuarioMedicoDAOMySQL();
    }

    public void adicionarProntuario(ProntuarioMedico prontuario) {
        try {
            prontuarioDAO.salvar(prontuario);
        } catch (Exception e) {
            System.err.println("Erro ao adicionar prontuário: " + e.getMessage());
        }
    }

    public List<ProntuarioMedico> listarProntuarios() {
        try {
            return prontuarioDAO.listarTodos();
        } catch (Exception e) {
            System.err.println("Erro ao listar prontuários: " + e.getMessage());
            return null;
        }
    }

    public ProntuarioMedico buscarPorId(int id) {
        try {
            return prontuarioDAO.buscarPorId(id);
        } catch (Exception e) {
            System.err.println("Erro ao buscar prontuário: " + e.getMessage());
            return null;
        }
    }

    public boolean atualizarProntuario(ProntuarioMedico prontuario) {
        try {
            return prontuarioDAO.atualizar(prontuario);
        } catch (Exception e) {
            System.err.println("Erro ao atualizar prontuario: " + e.getMessage());
            return false;
        }
    }

    public boolean removerProntuario(int id) {
        try {
            return prontuarioDAO.remover(id);
        } catch (Exception e) {
            System.err.println("Erro ao remover prontuário: " + e.getMessage());
            return false;
        }
    }
}
