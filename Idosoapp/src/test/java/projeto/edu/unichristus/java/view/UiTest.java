package projeto.edu.unichristus.java.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class UiTest {

    @Test
    void parseDateAceitaFormatoDoFormulario() {
        assertEquals(LocalDate.of(2026, 8, 14), Ui.parseDate("2026-08-14", "Data"));
        assertNull(Ui.parseDate(" ", "Data"));
    }

    @Test
    void parseDateRejeitaFormatoInvalidoComMensagemClara() {
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class, new org.junit.jupiter.api.function.Executable() {
            public void execute() {
                Ui.parseDate("14/08/2026", "Data");
            }
        });

        assertEquals("Data deve usar o formato yyyy-MM-dd.", erro.getMessage());
    }

    @Test
    void parseRequiredDateExigeValor() {
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class, new org.junit.jupiter.api.function.Executable() {
            public void execute() {
                Ui.parseRequiredDate("", "Data");
            }
        });

        assertEquals("Data e obrigatorio.", erro.getMessage());
    }

    @Test
    void parseDateTimeAceitaFormatoDoFormulario() {
        assertEquals(LocalDateTime.of(2026, 8, 14, 10, 30), Ui.parseDateTime("2026-08-14 10:30", "Data/hora"));
        assertNull(Ui.parseDateTime(null, "Data/hora"));
    }

    @Test
    void parseDateTimeRejeitaFormatoInvalidoComMensagemClara() {
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class, new org.junit.jupiter.api.function.Executable() {
            public void execute() {
                Ui.parseDateTime("2026-08-14T10:30", "Data/hora");
            }
        });

        assertEquals("Data/hora deve usar o formato yyyy-MM-dd HH:mm.", erro.getMessage());
    }

    @Test
    void parseRequiredDateTimeExigeValor() {
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class, new org.junit.jupiter.api.function.Executable() {
            public void execute() {
                Ui.parseRequiredDateTime(" ", "Data/hora");
            }
        });

        assertEquals("Data/hora e obrigatorio.", erro.getMessage());
    }
}
