package br.edu.unichristus.recantorsc.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import projeto.edu.unichristus.java.dao.ConsultaDAO;
import projeto.edu.unichristus.java.dao.EventoSentinelaDAO;
import projeto.edu.unichristus.java.dao.IdosaDAO;
import projeto.edu.unichristus.java.dao.PrescricaoDAO;
import projeto.edu.unichristus.java.dao.ProntuarioDAO;
import projeto.edu.unichristus.java.dao.VacinaDAO;
import projeto.edu.unichristus.java.model.Consulta;
import projeto.edu.unichristus.java.model.EventoSentinela;
import projeto.edu.unichristus.java.model.Idosa;
import projeto.edu.unichristus.java.model.Prescricao;
import projeto.edu.unichristus.java.model.ProfissionalSaude;
import projeto.edu.unichristus.java.model.ProntuarioMedico;
import projeto.edu.unichristus.java.model.TipoEventoSentinela;
import projeto.edu.unichristus.java.model.Vacina;

class InMemoryDAOTest {
    @Test
    void idosaDaoGeraIdAtualizaBuscaERemove() {
        IdosaDAO dao = new IdosaDAO();
        Idosa idosa = new Idosa(0, "Maria Teste", "11122233344", LocalDate.of(1948, 3, 4), null, null, LocalDate.of(2024, 1, 2));

        assertTrue(dao.salvar(idosa));
        assertNotEquals(0, idosa.getId());
        assertNotNull(dao.buscarPorId(idosa.getId()));

        idosa.setNome("Maria Atualizada");
        assertTrue(dao.atualizar(idosa));
        assertEquals("Maria Atualizada", dao.buscarPorId(idosa.getId()).getNome());

        assertTrue(dao.remover(idosa.getId()));
    }

    @Test
    void consultaDaoGeraIdAtualizaBuscaERemove() {
        ConsultaDAO dao = new ConsultaDAO();
        ProfissionalSaude profissional = new ProfissionalSaude(7, "Dra. Ana", "Geriatria", "CRM-1");
        Consulta consulta = new Consulta(0, LocalDateTime.of(2026, 1, 10, 9, 0), profissional, "Rotina", "Acompanhamento", "Estavel");

        assertTrue(dao.salvar(consulta));
        assertNotEquals(0, consulta.getId());

        consulta.setMotivo("Retorno");
        assertTrue(dao.atualizar(consulta));
        assertEquals("Retorno", dao.buscarPorId(consulta.getId()).getMotivo());

        assertTrue(dao.remover(consulta.getId()));
    }

    @Test
    void prontuarioDaoGeraIdAtualizaBuscaERemove() {
        ProntuarioDAO dao = new ProntuarioDAO();
        Idosa idosa = new Idosa();
        idosa.setId(12);
        idosa.setNome("Joana");
        ProntuarioMedico prontuario = new ProntuarioMedico();
        prontuario.setIdosa(idosa);
        prontuario.setDataHoraIdosa(LocalDateTime.of(2026, 2, 1, 8, 30));

        assertTrue(dao.salvar(prontuario));
        assertNotEquals(0, prontuario.getId());

        prontuario.setDataHoraIdosa(LocalDateTime.of(2026, 2, 2, 8, 30));
        assertTrue(dao.atualizar(prontuario));
        assertEquals(LocalDateTime.of(2026, 2, 2, 8, 30), dao.buscarPorId(prontuario.getId()).getDataHoraIdosa());

        assertTrue(dao.remover(prontuario.getId()));
    }

    @Test
    void registrosClinicosGeramIdAtualizamBuscamERemovem() {
        PrescricaoDAO prescricoes = new PrescricaoDAO();
        Prescricao prescricao = new Prescricao(0, "Medicamento A", "1x ao dia", "7 dias", null);
        assertTrue(prescricoes.salvar(prescricao));
        prescricao.setDuracao("10 dias");
        assertTrue(prescricoes.atualizar(prescricao));
        assertEquals("10 dias", prescricoes.buscarPorId(prescricao.getId()).getDuracao());
        assertTrue(prescricoes.remover(prescricao.getId()));

        VacinaDAO vacinas = new VacinaDAO();
        Vacina vacina = new Vacina(0, "Influenza", LocalDate.of(2026, 4, 1));
        assertTrue(vacinas.salvar(vacina));
        vacina.setNome("Influenza anual");
        assertTrue(vacinas.atualizar(vacina));
        assertEquals("Influenza anual", vacinas.buscarPorId(vacina.getId()).getNome());
        assertTrue(vacinas.remover(vacina.getId()));

        EventoSentinelaDAO eventos = new EventoSentinelaDAO();
        EventoSentinela evento = new EventoSentinela(0, TipoEventoSentinela.QUEDA, LocalDate.of(2026, 5, 1));
        assertTrue(eventos.salvar(evento));
        evento.setTipo(TipoEventoSentinela.OUTRO);
        assertTrue(eventos.atualizar(evento));
        assertEquals(TipoEventoSentinela.OUTRO, eventos.buscarPorId(evento.getId()).getTipo());
        assertTrue(eventos.remover(evento.getId()));
    }
}
