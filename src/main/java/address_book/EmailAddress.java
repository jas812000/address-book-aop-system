package address_book;

import java.util.Objects;

/**
 * Represents a labeled email address associated with a contact.
 *
 * <p>The label identifies the purpose of the email address, such as
 * Personal, Work, or a user-defined value. Validation is enforced as
 * a cross-cutting concern by the application's AspectJ aspects.</p>
 *
 * @author James Stevens
 * @version 1.0
 * @since 2026-09-18
 */
public class EmailAddress {

    private String label;
    private String email;

    /**
     * Constructs a labeled email address.
     *
     * @param label label describing the email address
     * @param email email address
     */
    public EmailAddress(String label, String email) {
        this.label = label;
        this.email = email;
    }

    /**
     * Creates a copy of an existing email address.
     *
     * @param other email address to copy
     */
    public EmailAddress(EmailAddress other) {
        Objects.requireNonNull(other, "Email address to copy cannot be null.");

        this.label = other.label;
        this.email = other.email;
    }

    /**
     * Returns the label describing this email address.
     *
     * @return email-address label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Changes the label describing this email address.
     *
     * <p>This mutation provides a join point where AspectJ can enforce
     * label validation before the value is changed.</p>
     *
     * @param label replacement email-address label
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Returns the email value.
     *
     * @return email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Changes the email value.
     *
     * <p>This mutation provides a join point where AspectJ can enforce
     * email validation before the value is changed.</p>
     *
     * @param email replacement email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns a copy of this email address.
     *
     * @return independent email-address copy
     */
    public EmailAddress copy() {
        return new EmailAddress(this);
    }
}
