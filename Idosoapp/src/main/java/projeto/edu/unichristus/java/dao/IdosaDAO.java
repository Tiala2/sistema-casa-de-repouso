package projeto.edu.unichristus.java.dao;

import projeto.edu.unichristus.java.model.Idosa;
import java.util.ArrayList;
import java.util.List;

public class IdosaDAO {
    private final List<Idosa> idosas = new ArrayList<>();
    private int nextId = 1;

    public boolean salvar(Idosa idosa) {
        if (idosa == null) {
            return false;
        }
        if (idosa.getId() == 0) {
            idosa.setId(nextId++);
        }
        idosas.add(idosa);
        return true;
    }

    public List<Idosa> listarTodos() {
        return new ArrayList<>(idosas);
    }

    public Idosa buscarPorId(int id) {
        for (Idosa idosa : idosas) {
            if (idosa.getId() == id) {
                return idosa;
            }
        }
        return null;
    }

    public boolean atualizar(Idosa idosa) {
        if (idosa == null) {
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
        return idosas.removeIf(i -> i.getId() == id);
    }
}
