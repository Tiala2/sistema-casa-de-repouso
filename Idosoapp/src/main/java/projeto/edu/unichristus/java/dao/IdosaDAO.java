package projeto.edu.unichristus.java.dao;

import projeto.edu.unichristus.java.model.Idosa;
import java.util.ArrayList;
import java.util.List;

public class IdosaDAO {
    private final List<Idosa> idosas = new ArrayList<>();
    private int nextId = 1;

    public boolean salvar(Idosa idosa) {
        if (idosa == null || !DaoValidations.hasText(idosa.getNome()) || !DaoValidations.hasText(idosa.getCpf())) {
            return false;
        }
        if (!atribuirIdSeDisponivel(idosa)) {
            return false;
        }
        idosas.add(idosa);
        return true;
    }

    public List<Idosa> listarTodos() {
        return new ArrayList<>(idosas);
    }

    public Idosa buscarPorId(int id) {
        if (!DaoValidations.positiveId(id)) {
            return null;
        }
        for (Idosa idosa : idosas) {
            if (idosa.getId() == id) {
                return idosa;
            }
        }
        return null;
    }

    public boolean atualizar(Idosa idosa) {
        if (idosa == null || !DaoValidations.positiveId(idosa.getId()) || !DaoValidations.hasText(idosa.getNome()) || !DaoValidations.hasText(idosa.getCpf())) {
            return false;
        }
        for (int i = 0; i < idosas.size(); i++) {
            if (idosas.get(i).getId() == idosa.getId()) {
                idosas.set(i, idosa);
                return true;
            }
        }
        return false;
    }

    public boolean remover(int id) {
        if (!DaoValidations.positiveId(id)) {
            return false;
        }
        return idosas.removeIf(i -> i.getId() == id);
    }

    private boolean atribuirIdSeDisponivel(Idosa idosa) {
        if (idosa.getId() < 0 || buscarPorId(idosa.getId()) != null) {
            return false;
        }
        if (idosa.getId() == 0) {
            idosa.setId(nextId++);
        } else if (idosa.getId() >= nextId) {
            nextId = idosa.getId() + 1;
        }
        return true;
    }
}
