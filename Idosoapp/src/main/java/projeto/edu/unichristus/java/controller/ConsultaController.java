package projeto.edu.unichristus.java.controller;

import projeto.edu.unichristus.java.model.Consulta;
import projeto.edu.unichristus.java.dao.ConsultaDAOMySQL;
import java.util.List;

public class ConsultaController {
    private ConsultaDAOMySQL consultaDAO;

    public ConsultaController() {
        this.consultaDAO = new ConsultaDAOMySQL();
    }

    public void adicionarConsulta(Consulta consulta) {
        try {
            consultaDAO.salvar(consulta);
        } catch (Exception e) {
            System.err.println("Erro ao adicionar consulta: " + e.getMessage());
        }
    }

    public List<Consulta> listarConsultas() {
        try {
            return consultaDAO.listarTodos();
        } catch (Exception e) {
            System.err.println("Erro ao listar consultas: " + e.getMessage());
            return null;
        }
    }

    public Consulta buscarPorId(int id) {
        try {
            return consultaDAO.buscarPorId(id);
        } catch (Exception e) {
            System.err.println("Erro ao buscar consulta: " + e.getMessage());
            return null;
        }
    }

    public boolean atualizarConsulta(Consulta consulta) {
        try {
            return consultaDAO.atualizar(consulta);
        } catch (Exception e) {
            System.err.println("Erro ao atualizar consulta: " + e.getMessage());
            return false;
        }
    }

    public boolean removerConsulta(int id) {
        try {
            return consultaDAO.remover(id);
        } catch (Exception e) {
            System.err.println("Erro ao remover consulta: " + e.getMessage());
            return false;
        }
    }
}
