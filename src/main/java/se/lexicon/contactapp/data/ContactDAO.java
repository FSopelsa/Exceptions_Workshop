package se.lexicon.contactapp.data;

import se.lexicon.contactapp.exception.ContactStorageException;
import se.lexicon.contactapp.exception.DuplicateContactException;
import se.lexicon.contactapp.model.Contact;

import java.util.List;

public interface ContactDAO {

    List<Contact> findAll() throws ContactStorageException;

    void save(Contact contact) throws ContactStorageException, DuplicateContactException;

    Contact findByName(String name) throws ContactStorageException;
}
