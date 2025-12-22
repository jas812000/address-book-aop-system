
/*
 * Author: James Stevens
 * Date: 01 July 2025
 * Course: SWEN 656 - Advanced Software Design and Implementation
 * 
 * Copyright (c) 2025 James Stevens
 * This file is part of the Address Book project and may not be used, copied,
 * modified, or distributed without permission.
 */

package address_book;

/**
 * Represents a single contact in the address book.
 * 
 * A Contact includes personal information (name, phone, email)
 * as well as mailing address (street, city, state, ZIP).
 * 
 * Supports standard getter/setter access and can be cloned via the copy constructor.
 * 
 * @author James Stevens
 * @version 1.0
 * @since 2025-07-01
 */
public class Contact {

	// Fields for contact details
	private String firstName;
	private String lastName;
	private String street;
	private String city;
	private String state;
	private String zipCode;
	private String phone;
	private String email;

	/**
	 * Constructs a contact with all fields initialized.
	 *
	 * @param firstName First name
	 * @param lastName  Last name
	 * @param street    Street address
	 * @param city      City name
	 * @param state     State name
	 * @param zipCode   ZIP code
	 * @param phone     Phone number
	 * @param email     Email address
	 */
	public Contact(String firstName, String lastName, String street, String city,
	               String state, String zipCode, String phone, String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.street = street;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.phone = phone;
		this.email = email;
	}

	/**
	 * Creates a copy of the given contact.
	 *
	 * @param other the contact to duplicate
	 */
	public Contact(Contact other) {
		this.firstName = other.firstName;
		this.lastName = other.lastName;
		this.street = other.street;
		this.city = other.city;
		this.state = other.state;
		this.zipCode = other.zipCode;
		this.phone = other.phone;
		this.email = other.email;
	}

	// Getters and setters
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Returns a full string representation of the contact.
	 *
	 * @return a multi-line string with all contact details
	 */
	@Override
	public String toString() {
		return "Name: " + firstName + " " + lastName +
		       "\nStreet: " + street +
		       "\nCity: " + city +
		       "\nState: " + state +
		       "\nZipCode: " + zipCode +
		       "\nPhone: " + phone +
		       "\nEmail: " + email;
	}
}



