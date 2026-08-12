package se.lexicon.contactapp.data;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import se.lexicon.contactapp.exception.ContactStorageException;
import se.lexicon.contactapp.exception.DuplicateContactException;
import se.lexicon.contactapp.model.Contact;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileContactDAOImplTest {

    @TempDir
    Path tempDirectory;

    @Test
    void saveAndFindAllPersistContacts() throws Exception {
        ContactDAO contactDAO = createDAO();
        Contact ada = new Contact("Ada Lovelace", "0123456789");
        Contact grace = new Contact("Grace Hopper", "9876543210");

        contactDAO.save(ada);
        contactDAO.save(grace);

        assertEquals(List.of(ada, grace), contactDAO.findAll());
    }

    @Test
    void findByNameIgnoresCaseAndSurroundingSpaces() throws Exception {
        ContactDAO contactDAO = createDAO();
        Contact contact = new Contact("Ada Lovelace", "0123456789");
        contactDAO.save(contact);

        assertEquals(contact, contactDAO.findByName("  ADA LOVELACE  "));
        assertNull(contactDAO.findByName("Grace Hopper"));
    }

    @Test
    void saveRejectsDuplicateNameOrPhoneNumber() throws Exception {
        ContactDAO contactDAO = createDAO();
        contactDAO.save(new Contact("Ada Lovelace", "0123456789"));

        assertThrows(
                DuplicateContactException.class,
                () -> contactDAO.save(new Contact("ada lovelace", "1111111111"))
        );
        assertThrows(
                DuplicateContactException.class,
                () -> contactDAO.save(new Contact("Grace Hopper", "0123456789"))
        );
    }

    @Test
    void saveAndReadPersistANameWithSpaces() throws Exception {
        ContactDAO contactDAO = createDAO();
        Contact contact = new Contact("Ada Lovelace", "0123456789");

        contactDAO.save(contact);

        assertEquals(List.of(contact), contactDAO.findAll());
    }

    @Test
    void findAllWrapsMalformedFileDataInCheckedException() throws IOException {
        Path file = tempDirectory.resolve("contacts.txt");
        Files.writeString(file, "not valid contact data");
        ContactDAO contactDAO = new FileContactDAOImpl(file);

        assertThrows(ContactStorageException.class, contactDAO::findAll);
    }

    private ContactDAO createDAO() {
        return new FileContactDAOImpl(tempDirectory.resolve("storage").resolve("contacts.txt"));
    }
}
