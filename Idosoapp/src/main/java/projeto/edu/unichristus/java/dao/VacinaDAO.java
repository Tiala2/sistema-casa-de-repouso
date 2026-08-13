package projeto.edu.unichristus.java.dao;

import projeto.edu.unichristus.java.model.Vacina;
import java.util.ArrayList;
import java.util.List;

public class VacinaDAO {
    private static List<Vacina> vacinas = new ArrayList<>();

    public void salvar(Vacina vacina) {
        vacinas.add(vacina);
    }

    public List<Vacina> listarTodos() {
        return new ArrayList<>(vacinas);
    }

    public Vacina buscarPorId(int id) {
        for (Vacina vacina : vacinas) {
            if (vacina.getId() == id) {
                return vacina;
            }
        }
        return null;
    }

    public boolean atualizar(Vacina vacina) {
        for (int i = 0; i < vacinas.size(); i++) {
            if (vacinas.get(i).getId() == vacina.getId()) {
                vacinas.set(i, vacina);
                return true;
            }
        }
        return false;
    }

    public boolean remover(int id) {
        return vacinas.removeIf(v -> v.getId() == id);
    }
}
