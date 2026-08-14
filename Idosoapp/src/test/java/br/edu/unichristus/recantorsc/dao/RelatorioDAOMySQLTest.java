package br.edu.unichristus.recantorsc.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import projeto.edu.unichristus.java.dao.EventoSentinelaDAOMySQL;
import projeto.edu.unichristus.java.dao.RelatorioDAOMySQL;
import projeto.edu.unichristus.java.model.EventoSentinela;
import projeto.edu.unichristus.java.model.TipoEventoSentinela;

class RelatorioDAOMySQLTest {
    @Test
    void percentualIdosasPorEventoConsultaCadaProntuarioUmaVez() {
        EventoDAOFake eventoDAO = new EventoDAOFake();
        eventoDAO.eventos.put(1, Arrays.asList(
            new EventoSentinela(1, TipoEventoSentinela.QUEDA, LocalDate.of(2026, 8, 1)),
            new EventoSentinela(2, TipoEventoSentinela.DIARREIA, LocalDate.of(2026, 8, 2))
        ));
        eventoDAO.eventos.put(2, Arrays.asList(
            new EventoSentinela(3, TipoEventoSentinela.QUEDA, LocalDate.of(2026, 8, 3)),
            new EventoSentinela(4, TipoEventoSentinela.QUEDA, LocalDate.of(2026, 8, 4))
        ));

        Map<TipoEventoSentinela, Double> percentual = new RelatorioDAOMySQL()
            .percentualIdosasPorEvento(Arrays.asList(1, 2), 8, 2026, eventoDAO);

        assertEquals(2, eventoDAO.quantidadeConsultas);
        assertEquals(100.0, percentual.get(TipoEventoSentinela.QUEDA), 0.001);
        assertEquals(50.0, percentual.get(TipoEventoSentinela.DIARREIA), 0.001);
        assertEquals(0.0, percentual.get(TipoEventoSentinela.OBITO), 0.001);
    }

    private static class EventoDAOFake extends EventoSentinelaDAOMySQL {
        private final Map<Integer, List<EventoSentinela>> eventos = new HashMap<>();
        private int quantidadeConsultas;

        @Override
        public List<EventoSentinela> listarPorIdosaEPeriodo(int prontuarioId, int mes, int ano) {
            quantidadeConsultas++;
            List<EventoSentinela> resultado = eventos.get(prontuarioId);
            return resultado != null ? resultado : Collections.emptyList();
        }
    }
}
