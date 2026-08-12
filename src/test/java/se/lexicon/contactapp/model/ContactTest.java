package se.lexicon.contactapp.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ContactTest {

    @Test
    void constructorCreatesContactWithValidValues() {
        Contact contact = new Contact("Ada Lovelace", "0123456789");

        assertEquals("Ada Lovelace", contact.getName());
        assertEquals("0123456789", contact.getPhoneNumber());
    }

    @Test
    void constructorTrimsAValidName() {
        Contact contact = new Contact("  Ada Lovelace  ", "0123456789");

        assertEquals("Ada Lovelace", contact.getName());
    }

    @Test
    void constructorRejectsNullOrBlankNames() {
        assertThrows(IllegalArgumentException.class, () -> new Contact(null, "0123456789"));
        assertThrows(IllegalArgumentException.class, () -> new Contact("   ", "0123456789"));
    }

    @Test
    void constructorRejectsPhoneNumbersThatAreNotExactlyTenDigits() {
        assertThrows(IllegalArgumentException.class, () -> new Contact("Ada", null));
        assertThrows(IllegalArgumentException.class, () -> new Contact("Ada", "123456789"));
        assertThrows(IllegalArgumentException.class, () -> new Contact("Ada", "12345678901"));
        assertThrows(IllegalArgumentException.class, () -> new Contact("Ada", "012-345-6789"));
    }

    @Test
    void settersApplyTheSameValidationRules() {
        Contact contact = new Contact("Ada", "0123456789");

        contact.setName("Grace Hopper");
        contact.setPhoneNumber("9876543210");

        assertEquals("Grace Hopper", contact.getName());
        assertEquals("9876543210", contact.getPhoneNumber());
        assertThrows(IllegalArgumentException.class, () -> contact.setName(""));
        assertThrows(IllegalArgumentException.class, () -> contact.setPhoneNumber("not-a-phone"));
    }
}
