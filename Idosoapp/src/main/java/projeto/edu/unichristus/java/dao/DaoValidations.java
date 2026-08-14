package projeto.edu.unichristus.java.dao;

final class DaoValidations {

    private DaoValidations() {
    }

    static boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    static boolean positiveId(int id) {
        return id > 0;
    }
}
