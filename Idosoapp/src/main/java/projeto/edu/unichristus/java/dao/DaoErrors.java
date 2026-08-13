package projeto.edu.unichristus.java.dao;

import java.sql.SQLException;

final class DaoErrors {

    private DaoErrors() {
    }

    static void log(String operation, SQLException error) {
        StringBuilder message = new StringBuilder("[DAO] ");
        message.append(operation)
            .append(" falhou: ")
            .append(error.getMessage());

        if (error.getSQLState() != null) {
            message.append(" | SQLState: ").append(error.getSQLState());
        }

        if (error.getErrorCode() != 0) {
            message.append(" | Codigo: ").append(error.getErrorCode());
        }

        System.err.println(message.toString());
    }
}
