package projeto.edu.unichristus.java.controller;

import projeto.edu.unichristus.java.model.Consulta;
import projeto.edu.unichristus.java.dao.ConsultaDAOMySQL;
import java.util.List;

public class ConsultaController {
    private final ConsultaDAOMySQL consultaDAO;

    public ConsultaController() {
        this.consultaDAO = new ConsultaDAOMySQL();
    }

    public boolean adicionarConsulta(Consulta consulta) {
        try {
            return consultaDAO.salvar(consulta);
        } catch (Exception e) {
            ControllerErrors.log("Adicionar consulta", e);
            return false;
        }
    }

    public List<Consulta> listarConsultas() {
        try {
            return ControllerErrors.listOrEmpty(consultaDAO.listarTodos());
        } catch (Exception e) {
            return ControllerErrors.emptyList("Listar consultas", e);
        }
    }

    public Consulta buscarPorId(int id) {
        try {
            return consultaDAO.buscarPorId(id);
        } catch (Exception e) {
            ControllerErrors.log("Buscar consulta", e);
            return null;
        }
    }

    public boolean atualizarConsulta(Consulta consulta) {
        try {
            return consultaDAO.atualizar(consulta);
        } catch (Exception e) {
            ControllerErrors.log("Atualizar consulta", e);
            return false;
        }
    }

    public boolean removerConsulta(int id) {
        try {
            return consultaDAO.remover(id);
        } catch (Exception e) {
            ControllerErrors.log("Remover consulta", e);
            return false;
        }
    }
}
