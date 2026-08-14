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

    static boolean validPeriod(int month, int year) {
        return month >= 1 && month <= 12 && year > 0;
    }
}
