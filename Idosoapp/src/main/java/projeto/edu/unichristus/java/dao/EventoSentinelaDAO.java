package projeto.edu.unichristus.java.dao;

import projeto.edu.unichristus.java.model.EventoSentinela;
import java.util.ArrayList;
import java.util.List;

public class EventoSentinelaDAO {
    private final List<EventoSentinela> eventos = new ArrayList<>();
    private int nextId = 1;

    public boolean salvar(EventoSentinela evento) {
        if (evento == null || evento.getTipo() == null || evento.getDataOcorrencia() == null) {
            return false;
        }
        if (!atribuirIdSeDisponivel(evento)) {
            return false;
        }
        eventos.add(evento);
        return true;
    }

    public List<EventoSentinela> listarTodos() {
        return new ArrayList<>(eventos);
    }

    public EventoSentinela buscarPorId(int id) {
        if (!DaoValidations.positiveId(id)) {
            return null;
        }
        for (EventoSentinela evento : eventos) {
            if (evento.getId() == id) {
                return evento;
            }
        }
        return null;
    }

    public boolean atualizar(EventoSentinela evento) {
        if (evento == null || !DaoValidations.positiveId(evento.getId()) || evento.getTipo() == null || evento.getDataOcorrencia() == null) {
            return false;
        }
        for (int i = 0; i < eventos.size(); i++) {
            if (eventos.get(i).getId() == evento.getId()) {
                eventos.set(i, evento);
                return true;
            }
        }
        return false;
    }

    public boolean remover(int id) {
        if (!DaoValidations.positiveId(id)) {
            return false;
        }
        return eventos.removeIf(e -> e.getId() == id);
    }

    private boolean atribuirIdSeDisponivel(EventoSentinela evento) {
        if (evento.getId() < 0 || buscarPorId(evento.getId()) != null) {
            return false;
        }
        if (evento.getId() == 0) {
            evento.setId(nextId++);
        } else if (evento.getId() >= nextId) {
            nextId = evento.getId() + 1;
        }
        return true;
    }
}
