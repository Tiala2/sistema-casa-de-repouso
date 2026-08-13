package projeto.edu.unichristus.java.controller;

final class ControllerErrors {

    private ControllerErrors() {
    }

    static void log(String operation, Exception error) {
        System.err.println("[Controller] " + operation + " falhou: " + error.getMessage());
    }
}
