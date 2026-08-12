package se.lexicon.contactapp;

import se.lexicon.contactapp.controller.ContactController;
import se.lexicon.contactapp.data.ContactDAO;
import se.lexicon.contactapp.data.FileContactDAOImpl;
import se.lexicon.contactapp.view.ContactView;

import java.nio.file.Path;

public class Main {

    public static void main(String[] args) {
        ContactDAO contactDAO = new FileContactDAOImpl(Path.of("data", "contacts.txt"));
        ContactView contactView = new ContactView();
        ContactController controller = new ContactController(contactDAO, contactView);

        controller.run();
    }
}
