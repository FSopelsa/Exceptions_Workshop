package se.lexicon.contactapp.exception;

/**
 * Signals that contact data could not be read from or written to storage.
 */
public class ContactStorageException extends Exception {

    public ContactStorageException(String message) {
        super(message);
    }

    public ContactStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
