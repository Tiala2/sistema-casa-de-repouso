package projeto.edu.unichristus.java.controller;

import java.util.Collections;
import java.util.List;

final class ControllerErrors {

    private ControllerErrors() {
    }

    static void log(String operation, Exception error) {
        System.err.println("[Controller] " + operation + " falhou: " + error.getMessage());
    }

    static <T> List<T> emptyList(String operation, Exception error) {
        log(operation, error);
        return Collections.emptyList();
    }

    static <T> List<T> listOrEmpty(List<T> result) {
        return result != null ? result : Collections.<T>emptyList();
    }
}
