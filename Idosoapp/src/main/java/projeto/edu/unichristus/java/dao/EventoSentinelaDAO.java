package projeto.edu.unichristus.java.dao;

import projeto.edu.unichristus.java.model.EventoSentinela;
import java.util.ArrayList;
import java.util.List;

public class EventoSentinelaDAO {
    private static List<EventoSentinela> eventos = new ArrayList<>();

    public void salvar(EventoSentinela evento) {
        eventos.add(evento);
    }

    public List<EventoSentinela> listarTodos() {
        return new ArrayList<>(eventos);
    }

    public EventoSentinela buscarPorId(int id) {
        for (EventoSentinela evento : eventos) {
            if (evento.getId() == id) {
                return evento;
            }
        }
        return null;
    }

    public boolean atualizar(EventoSentinela evento) {
        for (int i = 0; i < eventos.size(); i++) {
            if (eventos.get(i).getId() == evento.getId()) {
                eventos.set(i, evento);
                return true;
            }
        }
        return false;
    }

    public boolean remover(int id) {
        return eventos.removeIf(e -> e.getId() == id);
    }
}
