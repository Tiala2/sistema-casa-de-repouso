package projeto.edu.unichristus.java.dao;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.Test;

class DaoErrorsTest {

    @Test
    void emptyListNuncaRetornaNull() {
        PrintStream originalErr = System.err;
        try {
            System.setErr(new PrintStream(new ByteArrayOutputStream()));
            List<String> resultado = DaoErrors.emptyList("listar teste", new SQLException("falha simulada"));

            assertNotNull(resultado);
            assertTrue(resultado.isEmpty());
        } finally {
            System.setErr(originalErr);
        }
    }
}
