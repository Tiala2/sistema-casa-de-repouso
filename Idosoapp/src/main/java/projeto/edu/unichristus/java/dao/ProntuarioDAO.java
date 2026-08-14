package projeto.edu.unichristus.java.dao;

import projeto.edu.unichristus.java.model.ProntuarioMedico;
import java.util.ArrayList;
import java.util.List;

public class ProntuarioDAO {
    private final List<ProntuarioMedico> prontuarios = new ArrayList<>();
    private int nextId = 1;

    public boolean salvar(ProntuarioMedico prontuario) {
        if (prontuario == null || prontuario.getIdosa() == null || !DaoValidations.positiveId(prontuario.getIdosa().getId())) {
            return false;
        }
        if (!atribuirIdSeDisponivel(prontuario)) {
            return false;
        }
        prontuarios.add(prontuario);
        return true;
    }

    public List<ProntuarioMedico> listarTodos() {
        return new ArrayList<>(prontuarios);
    }

    public ProntuarioMedico buscarPorId(int id) {
        if (!DaoValidations.positiveId(id)) {
            return null;
        }
        for (ProntuarioMedico prontuario : prontuarios) {
            if (prontuario.getId() == id) {
                return prontuario;
            }
        }
        return null;
    }

    public boolean atualizar(ProntuarioMedico prontuario) {
        if (prontuario == null || !DaoValidations.positiveId(prontuario.getId()) || prontuario.getIdosa() == null || !DaoValidations.positiveId(prontuario.getIdosa().getId())) {
            return false;
        }
        for (int i = 0; i < prontuarios.size(); i++) {
            if (prontuarios.get(i).getId() == prontuario.getId()) {
                prontuarios.set(i, prontuario);
                return true;
            }
        }
        return false;
    }

    public boolean remover(int id) {
        if (!DaoValidations.positiveId(id)) {
            return false;
        }
        return prontuarios.removeIf(p -> p.getId() == id);
    }

    private boolean atribuirIdSeDisponivel(ProntuarioMedico prontuario) {
        if (prontuario.getId() < 0 || buscarPorId(prontuario.getId()) != null) {
            return false;
        }
        if (prontuario.getId() == 0) {
            prontuario.setId(nextId++);
        } else if (prontuario.getId() >= nextId) {
            nextId = prontuario.getId() + 1;
        }
        return true;
    }
}
