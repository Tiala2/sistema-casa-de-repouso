package projeto.edu.unichristus.java.dao;

import projeto.edu.unichristus.java.model.ProntuarioMedico;
import java.util.ArrayList;
import java.util.List;

public class ProntuarioDAO {
    private static List<ProntuarioMedico> prontuarios = new ArrayList<>();

    public boolean salvar(ProntuarioMedico prontuario) {
        prontuarios.add(prontuario);
        return true;
    }

    public List<ProntuarioMedico> listarTodos() {
        return new ArrayList<>(prontuarios);
    }

    public ProntuarioMedico buscarPorId(int id) {
        for (ProntuarioMedico prontuario : prontuarios) {
            if (prontuario.getId() == id) {
                return prontuario;
            }
        }
        return null;
    }

    public boolean atualizar(ProntuarioMedico prontuario) {
        for (int i = 0; i < prontuarios.size(); i++) {
            if (prontuarios.get(i).getId() == prontuario.getId()) {
                prontuarios.set(i, prontuario);
                return true;
            }
        }
        return false;
    }

    public boolean remover(int id) {
        return prontuarios.removeIf(p -> p.getId() == id);
    }
}
