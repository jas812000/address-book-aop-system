package address_book;

import java.util.Objects;

/**
 * Represents a labeled phone number associated with a contact.
 *
 * <p>The label identifies the purpose of the number, such as
 * Mobile, Home, Work, or a user-defined value. Validation and
 * normalization are enforced as cross-cutting concerns by the
 * application's AspectJ aspects.</p>
 *
 * @author James Stevens
 * @version 1.0
 * @since 2026-09-18
 */
public class PhoneNumber {

    private String label;
    private String number;

    /**
     * Constructs a labeled phone number.
     *
     * @param label label describing the phone number
     * @param number phone number
     */
    public PhoneNumber(String label, String number) {
        this.label = label;
        this.number = number;
    }

    /**
     * Creates a copy of an existing phone number.
     *
     * @param other phone number to copy
     */
    public PhoneNumber(PhoneNumber other) {
        Objects.requireNonNull(other, "Phone number to copy cannot be null.");

        this.label = other.label;
        this.number = other.number;
    }

    /**
     * Returns the label describing this phone number.
     *
     * @return phone-number label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Changes the label describing this phone number.
     *
     * <p>This mutation provides a join point where AspectJ can enforce
     * label validation before the value is changed.</p>
     *
     * @param label replacement phone-number label
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Returns the phone number.
     *
     * @return phone number
     */
    public String getNumber() {
        return number;
    }

    /**
     * Changes the phone number.
     *
     * <p>This mutation provides a join point where AspectJ can validate
     * and normalize the number before the value is changed.</p>
     *
     * @param number replacement phone number
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * Returns a copy of this phone number.
     *
     * @return independent phone-number copy
     */
    public PhoneNumber copy() {
        return new PhoneNumber(this);
    }
}
