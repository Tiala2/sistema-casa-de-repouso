package br.edu.unichristus.recantorsc.dao;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import projeto.edu.unichristus.java.dao.ConsultaDAOMySQL;
import projeto.edu.unichristus.java.dao.EventoSentinelaDAOMySQL;
import projeto.edu.unichristus.java.dao.IdosaDAOMySQL;
import projeto.edu.unichristus.java.dao.PrescricaoDAOMySQL;
import projeto.edu.unichristus.java.dao.ProfissionalSaudeDAOMySQL;
import projeto.edu.unichristus.java.dao.ProntuarioMedicoDAOMySQL;
import projeto.edu.unichristus.java.dao.RelatorioDAOMySQL;
import projeto.edu.unichristus.java.dao.VacinaDAOMySQL;
import projeto.edu.unichristus.java.model.Consulta;
import projeto.edu.unichristus.java.model.EventoSentinela;
import projeto.edu.unichristus.java.model.Idosa;
import projeto.edu.unichristus.java.model.Prescricao;
import projeto.edu.unichristus.java.model.ProfissionalSaude;
import projeto.edu.unichristus.java.model.ProntuarioMedico;
import projeto.edu.unichristus.java.model.Relatorio;
import projeto.edu.unichristus.java.model.TipoEventoSentinela;
import projeto.edu.unichristus.java.model.Vacina;

class MySQLDAOValidationTest {
    @Test
    void daosMysqlRecusamSalvarEAtualizarObjetoNuloAntesDaConexao() {
        assertFalse(new IdosaDAOMySQL().salvar(null));
        assertFalse(new IdosaDAOMySQL().atualizar(null));
        assertFalse(new ProfissionalSaudeDAOMySQL().salvar(null));
        assertFalse(new ProfissionalSaudeDAOMySQL().atualizar(null));
        assertFalse(new ConsultaDAOMySQL().salvar(null));
        assertFalse(new ConsultaDAOMySQL().atualizar(null));
        assertFalse(new ProntuarioMedicoDAOMySQL().salvar(null));
        assertFalse(new ProntuarioMedicoDAOMySQL().atualizar(null));
        assertFalse(new PrescricaoDAOMySQL().salvar(null, 1));
        assertFalse(new PrescricaoDAOMySQL().atualizar(null));
        assertFalse(new VacinaDAOMySQL().salvar(null, 1));
        assertFalse(new VacinaDAOMySQL().atualizar(null));
        assertFalse(new EventoSentinelaDAOMySQL().salvar(null, 1));
        assertFalse(new EventoSentinelaDAOMySQL().atualizar(null));
        assertFalse(new RelatorioDAOMySQL().salvar(null, 1));
        assertFalse(new RelatorioDAOMySQL().atualizar(null));
    }

    @Test
    void daosMysqlRecusamVinculosObrigatoriosInvalidosAntesDaConexao() {
        assertFalse(new ConsultaDAOMySQL().salvar(new Consulta()));
        assertFalse(new ConsultaDAOMySQL().atualizar(new Consulta()));
        assertFalse(new ProntuarioMedicoDAOMySQL().salvar(new ProntuarioMedico()));
        assertFalse(new ProntuarioMedicoDAOMySQL().atualizar(new ProntuarioMedico()));
        assertFalse(new PrescricaoDAOMySQL().salvar(null, 0));
        assertFalse(new VacinaDAOMySQL().salvar(null, 0));
        assertFalse(new EventoSentinelaDAOMySQL().salvar(new EventoSentinela(), 0));
        assertFalse(new RelatorioDAOMySQL().salvar(null, 0));
    }

    @Test
    void consultaMysqlRecusaDataObrigatoriaAusenteAntesDaConexao() {
        Consulta consulta = new Consulta();
        consulta.setProfissional(new ProfissionalSaude(1, "Dra. Ana", "Geriatria", "CRM-1"));

        assertFalse(new ConsultaDAOMySQL().salvar(consulta));
        assertFalse(new ConsultaDAOMySQL().atualizar(consulta));
    }

    @Test
    void daosMysqlRecusamCamposObrigatoriosEmBrancoAntesDaConexao() {
        assertFalse(new IdosaDAOMySQL().salvar(new Idosa(0, " ", "123", null, null, null, null)));
        assertFalse(new IdosaDAOMySQL().salvar(new Idosa(0, "Maria", " ", null, null, null, null)));
        assertFalse(new ProfissionalSaudeDAOMySQL().salvar(new ProfissionalSaude(0, " ", "Geriatria", "CRM-1")));
        assertFalse(new PrescricaoDAOMySQL().salvar(new Prescricao(0, " ", "1x", "7 dias", null), 1));
        assertFalse(new VacinaDAOMySQL().salvar(new Vacina(0, " ", LocalDate.of(2026, 8, 1)), 1));
        assertFalse(new RelatorioDAOMySQL().salvar(new Relatorio(0, " ", "Clinico"), 1));
        assertFalse(new RelatorioDAOMySQL().salvar(new Relatorio(0, "Descricao", " "), 1));
    }

    @Test
    void daosMysqlRecusamIdsDeVinculoZeradosAntesDaConexao() {
        Consulta consulta = new Consulta(0, LocalDateTime.of(2026, 8, 14, 10, 0), new ProfissionalSaude(0, "Dra. Ana", "Geriatria", "CRM-1"), "Rotina", null, null);
        ProntuarioMedico prontuario = new ProntuarioMedico();
        Idosa idosa = new Idosa();
        idosa.setId(0);
        prontuario.setIdosa(idosa);

        assertFalse(new ConsultaDAOMySQL().salvar(consulta));
        assertFalse(new ConsultaDAOMySQL().atualizar(consulta));
        assertFalse(new ProntuarioMedicoDAOMySQL().salvar(prontuario));
        assertFalse(new ProntuarioMedicoDAOMySQL().atualizar(prontuario));
    }

    @Test
    void daosMysqlRecusamDatasObrigatoriasAusentesAntesDaConexao() {
        assertFalse(new VacinaDAOMySQL().salvar(new Vacina(0, "Influenza", null), 1));
        assertFalse(new VacinaDAOMySQL().atualizar(new Vacina(1, "Influenza", null)));
        assertFalse(new EventoSentinelaDAOMySQL().salvar(new EventoSentinela(0, TipoEventoSentinela.QUEDA, null), 1));
        assertFalse(new EventoSentinelaDAOMySQL().atualizar(new EventoSentinela(1, TipoEventoSentinela.QUEDA, null)));
    }

    @Test
    void eventoSentinelaRecusaFiltrosInvalidosAntesDaConexao() {
        EventoSentinelaDAOMySQL dao = new EventoSentinelaDAOMySQL();

        assertTrue(dao.listarPorIdosaEPeriodo(0, 8, 2026).isEmpty());
        assertTrue(dao.listarPorIdosaEPeriodo(1, 0, 2026).isEmpty());
        assertTrue(dao.listarPorIdosaEPeriodo(1, 13, 2026).isEmpty());
        assertTrue(dao.listarPorIdosaEPeriodo(1, 8, 0).isEmpty());
        assertTrue(dao.listarPorTipoEPeriodo(null, 8, 2026).isEmpty());
        assertTrue(dao.listarPorTipoEPeriodo(null, 0, 0).isEmpty());
    }
}
