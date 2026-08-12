package se.lexicon.contactapp.controller;

import se.lexicon.contactapp.data.ContactDAO;
import se.lexicon.contactapp.exception.ContactStorageException;
import se.lexicon.contactapp.exception.DuplicateContactException;
import se.lexicon.contactapp.exception.ExceptionHandler;
import se.lexicon.contactapp.model.Contact;
import se.lexicon.contactapp.view.ContactView;

import java.util.NoSuchElementException;
import java.util.Objects;

public class ContactController {

    private final ContactDAO contactDAO;
    private final ContactView contactView;

    public ContactController(ContactDAO contactDAO, ContactView contactView) {
        this.contactDAO = Objects.requireNonNull(contactDAO, "Contact DAO must not be null.");
        this.contactView = Objects.requireNonNull(contactView, "Contact view must not be null.");
    }

    public void run() {
        boolean running = true;

        while (running) {
            try {
                contactView.displayMenu();
                String selection = contactView.getUserInput("Select an option: ").trim();

                switch (selection) {
                    case "1" -> listContacts();
                    case "2" -> addContact();
                    case "3" -> findContact();
                    case "0" -> {
                        contactView.displayMessage("Goodbye!");
                        running = false;
                    }
                    default -> contactView.displayError("Choose 0, 1, 2, or 3.");
                }
            } catch (NoSuchElementException exception) {
                contactView.displayMessage("Input closed. Goodbye!");
                running = false;
            } catch (Exception exception) {
                contactView.displayError(ExceptionHandler.handle(exception));
            }
        }
    }

    private void listContacts() throws ContactStorageException {
        contactView.displayContacts(contactDAO.findAll());
    }

    private void addContact() throws ContactStorageException, DuplicateContactException {
        String name = contactView.getUserInput("Name: ");
        String phoneNumber = contactView.getUserInput("Phone number (10 digits): ");
        Contact contact = new Contact(name, phoneNumber);

        contactDAO.save(contact);
        contactView.displayMessage("Contact saved.");
    }

    private void findContact() throws ContactStorageException {
        String name = contactView.getUserInput("Name to find: ");
        Contact contact = contactDAO.findByName(name);

        if (contact == null) {
            contactView.displayMessage("No contact found with that name.");
        } else {
            contactView.displayContacts(java.util.List.of(contact));
        }
    }
}
