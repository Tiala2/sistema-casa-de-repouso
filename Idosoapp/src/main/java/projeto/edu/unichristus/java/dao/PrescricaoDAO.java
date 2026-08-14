package projeto.edu.unichristus.java.dao;

import projeto.edu.unichristus.java.model.Prescricao;
import java.util.ArrayList;
import java.util.List;

public class PrescricaoDAO {
    private final List<Prescricao> prescricoes = new ArrayList<>();
    private int nextId = 1;

    public boolean salvar(Prescricao prescricao) {
        if (prescricao == null || !DaoValidations.hasText(prescricao.getMedicamento())) {
            return false;
        }
        if (prescricao.getId() == 0) {
            prescricao.setId(nextId++);
        }
        prescricoes.add(prescricao);
        return true;
    }

    public List<Prescricao> listarTodos() {
        return new ArrayList<>(prescricoes);
    }

    public Prescricao buscarPorId(int id) {
        for (Prescricao prescricao : prescricoes) {
            if (prescricao.getId() == id) {
                return prescricao;
            }
        }
        return null;
    }

    public boolean atualizar(Prescricao prescricao) {
        if (prescricao == null || !DaoValidations.hasText(prescricao.getMedicamento())) {
            return false;
        }
        for (int i = 0; i < prescricoes.size(); i++) {
            if (prescricoes.get(i).getId() == prescricao.getId()) {
                prescricoes.set(i, prescricao);
                return true;
            }
        }
        return false;
    }

    public boolean remover(int id) {
        return prescricoes.removeIf(p -> p.getId() == id);
    }
}
