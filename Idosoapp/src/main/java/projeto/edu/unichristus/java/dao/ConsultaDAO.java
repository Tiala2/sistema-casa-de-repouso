package projeto.edu.unichristus.java.dao;

import projeto.edu.unichristus.java.model.Consulta;
import java.util.ArrayList;
import java.util.List;

public class ConsultaDAO {
    private static List<Consulta> consultas = new ArrayList<>();
    private static int nextId = 1;

    public boolean salvar(Consulta consulta) {
        if (consulta.getId() == 0) {
            consulta.setId(nextId++);
        }
        consultas.add(consulta);
        return true;
    }

    public List<Consulta> listarTodos() {
        return new ArrayList<>(consultas);
    }

    public Consulta buscarPorId(int id) {
        for (Consulta consulta : consultas) {
            if (consulta.getId() == id) {
                return consulta;
            }
        }
        return null;
    }

    public boolean atualizar(Consulta consulta) {
        for (int i = 0; i < consultas.size(); i++) {
            if (consultas.get(i).getId() == consulta.getId()) {
                consultas.set(i, consulta);
                return true;
            }
        }
        return false;
    }

    public boolean remover(int id) {
        return consultas.removeIf(c -> c.getId() == id);
    }
}
