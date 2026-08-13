package projeto.edu.unichristus.java.dao;

import projeto.edu.unichristus.java.model.Idosa;
import java.util.ArrayList;
import java.util.List;

public class IdosaDAO {
    private static List<Idosa> idosas = new ArrayList<>();

    public void salvar(Idosa idosa) {
        idosas.add(idosa);
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
