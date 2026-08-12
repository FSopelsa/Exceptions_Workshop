package se.lexicon.contactapp.view;

import se.lexicon.contactapp.model.Contact;

import java.io.PrintStream;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class ContactView {

    private final Scanner scanner;
    private final PrintStream output;

    public ContactView() {
        this(new Scanner(System.in), System.out);
    }

    public ContactView(Scanner scanner, PrintStream output) {
        this.scanner = Objects.requireNonNull(scanner, "Scanner must not be null.");
        this.output = Objects.requireNonNull(output, "Output must not be null.");
    }

    public String getUserInput(String prompt) {
        output.print(prompt);
        return scanner.nextLine();
    }

    public void displayMenu() {
        output.println();
        output.println("Contact App");
        output.println("1. List contacts");
        output.println("2. Add contact");
        output.println("3. Find contact by name");
        output.println("0. Exit");
    }

    public void displayContacts(List<Contact> contacts) {
        if (contacts.isEmpty()) {
            displayMessage("No contacts found.");
            return;
        }

        output.println("Contacts:");
        contacts.forEach(contact -> output.printf(
                "- %s | %s%n",
                contact.getName(),
                contact.getPhoneNumber()
        ));
    }

    public void displayMessage(String message) {
        output.println(message);
    }

    public void displayError(String message) {
        output.println("Error: " + message);
    }
}
