package br.edu.unichristus.recantorsc.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
    void idosaDaoMantemDadosIsoladosPorInstancia() {
        IdosaDAO primeiroDao = new IdosaDAO();
        IdosaDAO segundoDao = new IdosaDAO();
        Idosa idosa = new Idosa(0, "Helena Teste", "99988877766", LocalDate.of(1950, 6, 8), null, null, LocalDate.of(2025, 3, 2));

        assertTrue(primeiroDao.salvar(idosa));

        assertEquals(1, primeiroDao.listarTodos().size());
        assertTrue(segundoDao.listarTodos().isEmpty());
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

    @Test
    void daosEmMemoriaRecusamSalvarEAtualizarObjetoNulo() {
        assertFalse(new IdosaDAO().salvar(null));
        assertFalse(new IdosaDAO().atualizar(null));
        assertFalse(new ConsultaDAO().salvar(null));
        assertFalse(new ConsultaDAO().atualizar(null));
        assertFalse(new ProntuarioDAO().salvar(null));
        assertFalse(new ProntuarioDAO().atualizar(null));
        assertFalse(new PrescricaoDAO().salvar(null));
        assertFalse(new PrescricaoDAO().atualizar(null));
        assertFalse(new VacinaDAO().salvar(null));
        assertFalse(new VacinaDAO().atualizar(null));
        assertFalse(new EventoSentinelaDAO().salvar(null));
        assertFalse(new EventoSentinelaDAO().atualizar(null));
    }

    @Test
    void daosEmMemoriaRecusamCamposObrigatoriosInvalidos() {
        assertFalse(new IdosaDAO().salvar(new Idosa(0, " ", "123", null, null, null, null)));
        assertFalse(new IdosaDAO().salvar(new Idosa(0, "Maria", " ", null, null, null, null)));
        assertFalse(new PrescricaoDAO().salvar(new Prescricao(0, " ", "1x", "7 dias", null)));
        assertFalse(new VacinaDAO().salvar(new Vacina(0, " ", LocalDate.of(2026, 8, 1))));
        assertFalse(new VacinaDAO().salvar(new Vacina(0, "Influenza", null)));
        assertFalse(new EventoSentinelaDAO().salvar(new EventoSentinela(0, null, LocalDate.of(2026, 8, 1))));
        assertFalse(new EventoSentinelaDAO().salvar(new EventoSentinela(0, TipoEventoSentinela.QUEDA, null)));
    }

    @Test
    void daosEmMemoriaRecusamVinculosObrigatoriosInvalidos() {
        assertFalse(new ConsultaDAO().salvar(new Consulta(0, LocalDateTime.of(2026, 8, 14, 10, 0), null, "Rotina", null, null)));
        assertFalse(new ConsultaDAO().salvar(new Consulta(0, null, new ProfissionalSaude(1, "Dra. Ana", "Geriatria", "CRM-1"), "Rotina", null, null)));
        assertFalse(new ConsultaDAO().salvar(new Consulta(0, LocalDateTime.of(2026, 8, 14, 10, 0), new ProfissionalSaude(0, "Dra. Ana", "Geriatria", "CRM-1"), "Rotina", null, null)));

        ProntuarioMedico prontuario = new ProntuarioMedico();
        prontuario.setIdosa(new Idosa());
        assertFalse(new ProntuarioDAO().salvar(prontuario));
    }

    @Test
    void daosEmMemoriaRecusamBuscaERemocaoComIdInvalido() {
        assertNull(new IdosaDAO().buscarPorId(0));
        assertFalse(new IdosaDAO().remover(0));
        assertNull(new ConsultaDAO().buscarPorId(-1));
        assertFalse(new ConsultaDAO().remover(-1));
        assertNull(new ProntuarioDAO().buscarPorId(0));
        assertFalse(new ProntuarioDAO().remover(0));
        assertNull(new PrescricaoDAO().buscarPorId(-1));
        assertFalse(new PrescricaoDAO().remover(-1));
        assertNull(new VacinaDAO().buscarPorId(0));
        assertFalse(new VacinaDAO().remover(0));
        assertNull(new EventoSentinelaDAO().buscarPorId(-1));
        assertFalse(new EventoSentinelaDAO().remover(-1));
    }

    @Test
    void daosEmMemoriaRecusamAtualizacaoSemIdValido() {
        assertFalse(new IdosaDAO().atualizar(new Idosa(0, "Maria", "123", null, null, null, null)));
        assertFalse(new ConsultaDAO().atualizar(new Consulta(0, LocalDateTime.of(2026, 8, 14, 10, 0), new ProfissionalSaude(1, "Dra. Ana", "Geriatria", "CRM-1"), "Rotina", null, null)));

        ProntuarioMedico prontuario = new ProntuarioMedico();
        Idosa idosa = new Idosa();
        idosa.setId(1);
        prontuario.setIdosa(idosa);
        assertFalse(new ProntuarioDAO().atualizar(prontuario));

        assertFalse(new PrescricaoDAO().atualizar(new Prescricao(0, "Medicamento", "1x", "7 dias", null)));
        assertFalse(new VacinaDAO().atualizar(new Vacina(0, "Influenza", LocalDate.of(2026, 8, 1))));
        assertFalse(new EventoSentinelaDAO().atualizar(new EventoSentinela(0, TipoEventoSentinela.QUEDA, LocalDate.of(2026, 8, 1))));
    }

    @Test
    void daosEmMemoriaRecusamIdsDuplicadosAoSalvar() {
        IdosaDAO dao = new IdosaDAO();

        assertTrue(dao.salvar(new Idosa(10, "Maria", "123", null, null, null, null)));
        assertFalse(dao.salvar(new Idosa(10, "Ana", "456", null, null, null, null)));
        assertEquals(1, dao.listarTodos().size());
    }

    @Test
    void daosEmMemoriaAvancamIdAutomaticoAposIdInformado() {
        PrescricaoDAO dao = new PrescricaoDAO();
        Prescricao informada = new Prescricao(5, "Medicamento A", null, null, null);
        Prescricao automatica = new Prescricao(0, "Medicamento B", null, null, null);

        assertTrue(dao.salvar(informada));
        assertTrue(dao.salvar(automatica));

        assertEquals(6, automatica.getId());
    }
}
