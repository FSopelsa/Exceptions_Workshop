package se.lexicon.contactapp.exception;

/**
 * Signals an attempt to store a contact that already exists.
 */
public class DuplicateContactException extends Exception {

    public DuplicateContactException(String message) {
        super(message);
    }

    public DuplicateContactException(String message, Throwable cause) {
        super(message, cause);
    }
}
