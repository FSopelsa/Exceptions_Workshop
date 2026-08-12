package se.lexicon.contactapp.data;

import se.lexicon.contactapp.exception.ContactStorageException;
import se.lexicon.contactapp.exception.DuplicateContactException;
import se.lexicon.contactapp.model.Contact;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileContactDAOImpl implements ContactDAO {

    private static final String SEPARATOR = ";";

    private final Path filePath;

    public FileContactDAOImpl(Path filePath) {
        this.filePath = Objects.requireNonNull(filePath, "File path must not be null.");
    }

    @Override
    public List<Contact> findAll() throws ContactStorageException {
        List<Contact> contacts = new ArrayList<>();

        if (Files.notExists(filePath)) {
            return contacts;
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    contacts.add(toContact(line));
                }
            }
            return contacts;
        } catch (IOException exception) {
            throw new ContactStorageException("Could not read contacts.", exception);
        }
    }

    @Override
    public void save(Contact contact) throws ContactStorageException, DuplicateContactException {
        Objects.requireNonNull(contact, "Contact must not be null.");

        boolean duplicate = findAll().stream().anyMatch(storedContact ->
                storedContact.getName().equalsIgnoreCase(contact.getName())
                        || storedContact.getPhoneNumber().equals(contact.getPhoneNumber())
        );
        if (duplicate) {
            throw new DuplicateContactException("A contact with that name or phone number already exists.");
        }

        try {
            Path parent = filePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            try (BufferedWriter writer = Files.newBufferedWriter(
                    filePath,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            )) {
                writer.write(contact.getName() + SEPARATOR + contact.getPhoneNumber());
                writer.newLine();
            }
        } catch (IOException exception) {
            throw new ContactStorageException("Could not save contact.", exception);
        }
    }

    @Override
    public Contact findByName(String name) throws ContactStorageException {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Search name must not be blank.");
        }

        return findAll().stream()
                .filter(contact -> contact.getName().equalsIgnoreCase(name.trim()))
                .findFirst()
                .orElse(null);
    }

    private Contact toContact(String line) throws ContactStorageException {
        String[] values = line.split(SEPARATOR, 2);
        if (values.length != 2) {
            throw new ContactStorageException("Contact file contains invalid data.");
        }

        try {
            return new Contact(values[0], values[1]);
        } catch (IllegalArgumentException exception) {
            throw new ContactStorageException("Contact file contains invalid data.", exception);
        }
    }
}
