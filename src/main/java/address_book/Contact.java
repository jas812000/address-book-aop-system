package address_book;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a contact in the address book.
 *
 * <p>A contact contains required first and last names together with zero or
 * more labeled addresses, phone numbers, and email addresses. Collection
 * values are defensively copied so callers cannot modify the contact's
 * internal state without using its controlled mutation methods.</p>
 *
 * <p>Field validation is enforced as a cross-cutting concern through
 * AspectJ at construction and mutation join points.</p>
 *
 * @author James Stevens
 * @version 2.0
 * @since 2025-07-01
 */
public class Contact {

	private String firstName;
	private String lastName;

	private final List<Address> addresses;
	private final List<PhoneNumber> phoneNumbers;
	private final List<EmailAddress> emailAddresses;

	/**
	 * Constructs a contact.
	 *
	 * @param firstName first name
	 * @param lastName last name
	 * @param addresses physical addresses
	 * @param phoneNumbers phone numbers
	 * @param emailAddresses email addresses
	 */
	public Contact(
			String firstName,
			String lastName,
			List<Address> addresses,
			List<PhoneNumber> phoneNumbers,
			List<EmailAddress> emailAddresses) {

		this.firstName = firstName;
		this.lastName = lastName;

		this.addresses = copyAddresses(addresses);
		this.phoneNumbers = copyPhoneNumbers(phoneNumbers);
		this.emailAddresses = copyEmailAddresses(emailAddresses);
	}

	/**
	 * Creates a deep copy of another contact.
	 *
	 * @param other contact to copy
	 */
	public Contact(Contact other) {
		Objects.requireNonNull(
				other,
				"Contact to copy cannot be null."
		);

		this.firstName = other.firstName;
		this.lastName = other.lastName;

		this.addresses = copyAddresses(other.addresses);
		this.phoneNumbers = copyPhoneNumbers(other.phoneNumbers);
		this.emailAddresses =
				copyEmailAddresses(other.emailAddresses);
	}

	/**
	 * Returns the first name.
	 *
	 * @return first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Changes the first name.
	 *
	 * <p>This method provides a mutation join point where AspectJ can
	 * enforce first-name validation.</p>
	 *
	 * @param firstName replacement first name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Returns the last name.
	 *
	 * @return last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Changes the last name.
	 *
	 * <p>This method provides a mutation join point where AspectJ can
	 * enforce last-name validation.</p>
	 *
	 * @param lastName replacement last name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Returns defensive copies of the contact's addresses.
	 *
	 * @return unmodifiable address list
	 */
	public List<Address> getAddresses() {
		return Collections.unmodifiableList(
				copyAddresses(addresses)
		);
	}

	/**
	 * Returns defensive copies of the contact's phone numbers.
	 *
	 * @return unmodifiable phone-number list
	 */
	public List<PhoneNumber> getPhoneNumbers() {
		return Collections.unmodifiableList(
				copyPhoneNumbers(phoneNumbers)
		);
	}

	/**
	 * Returns defensive copies of the contact's email addresses.
	 *
	 * @return unmodifiable email-address list
	 */
	public List<EmailAddress> getEmailAddresses() {
		return Collections.unmodifiableList(
				copyEmailAddresses(emailAddresses)
		);
	}

	/**
	 * Adds an address.
	 *
	 * @param address address to add
	 */
	public void addAddress(Address address) {
		addresses.add(
				Objects.requireNonNull(
						address,
						"Address cannot be null."
				).copy()
		);
	}

	/**
	 * Replaces an address at the specified position.
	 *
	 * @param index address index
	 * @param address replacement address
	 */
	public void replaceAddress(int index, Address address) {
		addresses.set(
				index,
				Objects.requireNonNull(
						address,
						"Address cannot be null."
				).copy()
		);
	}

	/**
	 * Removes an address.
	 *
	 * @param index address index
	 */
	public void removeAddress(int index) {
		addresses.remove(index);
	}

	/**
	 * Adds a phone number.
	 *
	 * @param phoneNumber phone number to add
	 */
	public void addPhoneNumber(PhoneNumber phoneNumber) {
		phoneNumbers.add(
				Objects.requireNonNull(
						phoneNumber,
						"Phone number cannot be null."
				).copy()
		);
	}

	/**
	 * Replaces a phone number at the specified position.
	 *
	 * @param index phone-number index
	 * @param phoneNumber replacement phone number
	 */
	public void replacePhoneNumber(
			int index,
			PhoneNumber phoneNumber) {

		phoneNumbers.set(
				index,
				Objects.requireNonNull(
						phoneNumber,
						"Phone number cannot be null."
				).copy()
		);
	}

	/**
	 * Removes a phone number.
	 *
	 * @param index phone-number index
	 */
	public void removePhoneNumber(int index) {
		phoneNumbers.remove(index);
	}

	/**
	 * Adds an email address.
	 *
	 * @param emailAddress email address to add
	 */
	public void addEmailAddress(EmailAddress emailAddress) {
		emailAddresses.add(
				Objects.requireNonNull(
						emailAddress,
						"Email address cannot be null."
				).copy()
		);
	}

	/**
	 * Replaces an email address at the specified position.
	 *
	 * @param index email-address index
	 * @param emailAddress replacement email address
	 */
	public void replaceEmailAddress(
			int index,
			EmailAddress emailAddress) {

		emailAddresses.set(
				index,
				Objects.requireNonNull(
						emailAddress,
						"Email address cannot be null."
				).copy()
		);
	}

	/**
	 * Removes an email address.
	 *
	 * @param index email-address index
	 */
	public void removeEmailAddress(int index) {
		emailAddresses.remove(index);
	}

	/**
	 * Returns the contact's full name.
	 *
	 * @return first and last name
	 */
	public String getFullName() {
		return firstName + " " + lastName;
	}

	/**
	 * Creates a deep copy of this contact.
	 *
	 * @return independent contact copy
	 */
	public Contact copy() {
		return new Contact(this);
	}

	/**
	 * Returns the basic domain representation of this contact.
	 *
	 * <p>The application's {@code DisplayAspect} may weave a richer
	 * presentation at {@code toString()} call sites.</p>
	 *
	 * @return contact's full name
	 */
	@Override
	public String toString() {
		return getFullName();
	}

	/**
	 * Creates deep copies of an address collection.
	 *
	 * @param source source addresses
	 * @return independent mutable list
	 */
	private static List<Address> copyAddresses(
			List<Address> source) {

		List<Address> copies = new ArrayList<>();

		if (source != null) {
			for (Address address : source) {
				copies.add(
						Objects.requireNonNull(
								address,
								"Address cannot be null."
						).copy()
				);
			}
		}

		return copies;
	}

	/**
	 * Creates deep copies of a phone-number collection.
	 *
	 * @param source source phone numbers
	 * @return independent mutable list
	 */
	private static List<PhoneNumber> copyPhoneNumbers(
			List<PhoneNumber> source) {

		List<PhoneNumber> copies = new ArrayList<>();

		if (source != null) {
			for (PhoneNumber phoneNumber : source) {
				copies.add(
						Objects.requireNonNull(
								phoneNumber,
								"Phone number cannot be null."
						).copy()
				);
			}
		}

		return copies;
	}

	/**
	 * Creates deep copies of an email-address collection.
	 *
	 * @param source source email addresses
	 * @return independent mutable list
	 */
	private static List<EmailAddress> copyEmailAddresses(
			List<EmailAddress> source) {

		List<EmailAddress> copies = new ArrayList<>();

		if (source != null) {
			for (EmailAddress emailAddress : source) {
				copies.add(
						Objects.requireNonNull(
								emailAddress,
								"Email address cannot be null."
						).copy()
				);
			}
		}

		return copies;
	}
}
