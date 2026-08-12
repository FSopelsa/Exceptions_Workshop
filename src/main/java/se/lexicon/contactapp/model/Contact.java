package se.lexicon.contactapp.model;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * A contact with a validated name and a ten-digit phone number.
 */
public class Contact {

    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^\\d{10}$");

    private String name;
    private String phoneNumber;

    public Contact(String name, String phoneNumber) {
        setName(name);
        setPhoneNumber(phoneNumber);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must not be blank.");
        }
        this.name = name.trim();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || !PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches()) {
            throw new IllegalArgumentException("Phone number must contain exactly 10 digits.");
        }
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Contact other)) {
            return false;
        }
        return name.equals(other.name) && phoneNumber.equals(other.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phoneNumber);
    }

    @Override
    public String toString() {
        return "Contact{name='%s', phoneNumber='%s'}".formatted(name, phoneNumber);
    }
}
