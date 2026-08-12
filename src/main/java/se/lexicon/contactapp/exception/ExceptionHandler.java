package se.lexicon.contactapp.exception;

public final class ExceptionHandler {

    private ExceptionHandler() {
    }

    public static String handle(Exception exception) {
        if (exception instanceof DuplicateContactException
                || exception instanceof ContactStorageException
                || exception instanceof IllegalArgumentException) {
            return exception.getMessage();
        }
        return "An unexpected error occurred. Please try again.";
    }
}
