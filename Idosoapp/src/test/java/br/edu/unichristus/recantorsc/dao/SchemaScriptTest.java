package br.edu.unichristus.recantorsc.dao;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

class SchemaScriptTest {

    @Test
    void scriptSqlMantemCamposObrigatoriosDaAplicacaoComoNotNull() throws IOException {
        String sql = new String(Files.readAllBytes(Paths.get("..", "criar_banco_idosoapp.sql")), StandardCharsets.UTF_8)
            .replace("\r\n", "\n");

        assertTrue(sql.contains("data_hora DATETIME NOT NULL"));
        assertTrue(sql.contains("medicamento VARCHAR(100) NOT NULL"));
        assertTrue(sql.contains("nome VARCHAR(100) NOT NULL,\n    data_ocorrencia DATE NOT NULL"));
        assertTrue(sql.contains("tipo VARCHAR(100) NOT NULL,\n    data_ocorrencia DATE NOT NULL"));
        assertTrue(sql.contains("descricao VARCHAR(200) NOT NULL,\n    tipo VARCHAR(100) NOT NULL"));
    }
}
