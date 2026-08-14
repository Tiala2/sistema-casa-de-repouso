package projeto.edu.unichristus.java.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.junit.jupiter.api.Test;

class ControllerErrorsTest {
    @Test
    void emptyListRetornaListaVaziaEmFalhaDeListagem() {
        PrintStream originalErr = System.err;
        ByteArrayOutputStream capturedErr = new ByteArrayOutputStream();
        System.setErr(new PrintStream(capturedErr));

        List<String> result;
        try {
            result = ControllerErrors.emptyList("Listar teste", new RuntimeException("falha"));
        } finally {
            System.setErr(originalErr);
        }

        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertTrue(capturedErr.toString().contains("Listar teste"));
    }

    @Test
    void listOrEmptyProtegeControllerContraListaNula() {
        List<String> result = ControllerErrors.listOrEmpty(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
