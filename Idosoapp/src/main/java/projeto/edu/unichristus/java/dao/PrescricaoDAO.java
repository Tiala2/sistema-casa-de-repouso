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
        if (!atribuirIdSeDisponivel(prescricao)) {
            return false;
        }
        prescricoes.add(prescricao);
        return true;
    }

    public List<Prescricao> listarTodos() {
        return new ArrayList<>(prescricoes);
    }

    public Prescricao buscarPorId(int id) {
        if (!DaoValidations.positiveId(id)) {
            return null;
        }
        for (Prescricao prescricao : prescricoes) {
            if (prescricao.getId() == id) {
                return prescricao;
            }
        }
        return null;
    }

    public boolean atualizar(Prescricao prescricao) {
        if (prescricao == null || !DaoValidations.positiveId(prescricao.getId()) || !DaoValidations.hasText(prescricao.getMedicamento())) {
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
        if (!DaoValidations.positiveId(id)) {
            return false;
        }
        return prescricoes.removeIf(p -> p.getId() == id);
    }

    private boolean atribuirIdSeDisponivel(Prescricao prescricao) {
        if (prescricao.getId() < 0 || buscarPorId(prescricao.getId()) != null) {
            return false;
        }
        if (prescricao.getId() == 0) {
            prescricao.setId(nextId++);
        } else if (prescricao.getId() >= nextId) {
            nextId = prescricao.getId() + 1;
        }
        return true;
    }
}
