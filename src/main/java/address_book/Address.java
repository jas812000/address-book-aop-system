package address_book;

import java.util.Objects;

/**
 * Represents a labeled physical address associated with a contact.
 *
 * <p>The label identifies the purpose of the address, such as
 * Home, Work, or a user-defined value. Validation of address data
 * is enforced as a cross-cutting concern by the application's
 * AspectJ validation aspects.</p>
 *
 * @author James Stevens
 * @version 1.0
 * @since 2026-09-18
 */
public class Address {

    private String label;
    private String street;
    private String city;
    private String state;
    private String zipCode;

    /**
     * Constructs a labeled physical address.
     *
     * @param label label describing the address
     * @param street street address
     * @param city city
     * @param state state
     * @param zipCode ZIP code
     */
    public Address(
            String label,
            String street,
            String city,
            String state,
            String zipCode) {

        this.label = label;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    /**
     * Creates a copy of an existing address.
     *
     * @param other address to copy
     */
    public Address(Address other) {
        Objects.requireNonNull(other, "Address to copy cannot be null.");

        this.label = other.label;
        this.street = other.street;
        this.city = other.city;
        this.state = other.state;
        this.zipCode = other.zipCode;
    }

    /**
     * Returns the label describing this address.
     *
     * @return address label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Changes the label describing this address.
     *
     * <p>This mutation provides a join point where AspectJ can enforce
     * label validation before the value is changed.</p>
     *
     * @param label replacement address label
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Returns the street portion of this address.
     *
     * @return street address
     */
    public String getStreet() {
        return street;
    }

    /**
     * Changes the street portion of this address.
     *
     * <p>This mutation provides a join point where AspectJ can enforce
     * street-address validation before the value is changed.</p>
     *
     * @param street replacement street address
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * Returns the city associated with this address.
     *
     * @return city
     */
    public String getCity() {
        return city;
    }

    /**
     * Changes the city associated with this address.
     *
     * <p>This mutation provides a join point where AspectJ can enforce
     * city validation before the value is changed.</p>
     *
     * @param city replacement city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Returns the state associated with this address.
     *
     * @return state
     */
    public String getState() {
        return state;
    }

    /**
     * Changes the state associated with this address.
     *
     * <p>This mutation provides a join point where AspectJ can enforce
     * state validation before the value is changed.</p>
     *
     * @param state replacement state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Returns the ZIP code associated with this address.
     *
     * @return ZIP code
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * Changes the ZIP code associated with this address.
     *
     * <p>This mutation provides a join point where AspectJ can enforce
     * ZIP-code validation before the value is changed.</p>
     *
     * @param zipCode replacement ZIP code
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    /**
     * Returns a copy of this address.
     *
     * @return independent address copy
     */
    public Address copy() {
        return new Address(this);
    }
}