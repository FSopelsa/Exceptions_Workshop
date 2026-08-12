package se.lexicon.contactapp.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import se.lexicon.contactapp.data.ContactDAO;
import se.lexicon.contactapp.data.FileContactDAOImpl;
import se.lexicon.contactapp.view.ContactView;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ContactControllerTest {

    @TempDir
    Path tempDirectory;

    @Test
    void runCoordinatesAddListFindAndExit() {
        String userInput = String.join(System.lineSeparator(),
                "2", "Ada Lovelace", "0123456789",
                "1",
                "3", "ada lovelace",
                "0"
        ) + System.lineSeparator();
        ByteArrayOutputStream outputBytes = new ByteArrayOutputStream();
        ContactView view = new ContactView(
                new Scanner(userInput),
                new PrintStream(outputBytes, true, StandardCharsets.UTF_8)
        );
        ContactDAO contactDAO = new FileContactDAOImpl(tempDirectory.resolve("contacts.txt"));
        ContactController controller = new ContactController(contactDAO, view);

        controller.run();

        String output = outputBytes.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("Contact saved."));
        assertTrue(output.contains("Ada Lovelace | 0123456789"));
        assertTrue(output.contains("Goodbye!"));
    }

    @Test
    void runDisplaysHandledErrorAndContinuesAfterDuplicate() {
        String userInput = String.join(System.lineSeparator(),
                "2", "Ada", "0123456789",
                "2", "ada", "1111111111",
                "0"
        ) + System.lineSeparator();
        ByteArrayOutputStream outputBytes = new ByteArrayOutputStream();
        ContactView view = new ContactView(
                new Scanner(userInput),
                new PrintStream(outputBytes, true, StandardCharsets.UTF_8)
        );
        ContactDAO contactDAO = new FileContactDAOImpl(tempDirectory.resolve("contacts.txt"));
        ContactController controller = new ContactController(contactDAO, view);

        controller.run();

        String output = outputBytes.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("Error: A contact with that name or phone number already exists."));
        assertTrue(output.contains("Goodbye!"));
    }
}
