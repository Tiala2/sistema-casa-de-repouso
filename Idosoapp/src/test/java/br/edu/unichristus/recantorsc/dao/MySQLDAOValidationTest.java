package br.edu.unichristus.recantorsc.dao;

import static org.junit.jupiter.api.Assertions.assertFalse;

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
import projeto.edu.unichristus.java.model.ProntuarioMedico;

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
}
