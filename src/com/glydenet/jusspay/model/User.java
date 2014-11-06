package com.glydenet.jusspay.model;

public class User {

	/**
	 * Constants
	 */
	public static final byte ACTIVE = 1;
	public static final byte INACTIVE = 0;

	private String firstName, lastName;
	private String email;
	private String password;
	private String userName;

	private int id;
	private String apiKey;
	private String dob;
	private byte status;
	private String created, modified;
	private String phone;
	private String portrait;

	/**
	 * @return the portrait
	 */
	public String getPortrait() {
		return portrait;
	}

	/**
	 * @param portrait the portrait to set
	 */
	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}

	public User() {
		super();
	}

	public User(String firstName, String lastName, String email,
			String password, String userName, String phone) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.userName = userName;
		this.phone = phone;
	}
	
	public User(String firstName, String lastName, String email,
			String userName, String phone) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.userName = userName;
		this.phone = phone;
	}

	public User(String firstName, String lastName, String apiKey, String email,
			String userName, String phone, String dob, byte status, String created,
			String modified) {
		this.firstName 		=	firstName;
		this.lastName		=	lastName;
		this.apiKey			=	apiKey;
		this.email			=	email;
		this.userName		=	userName;
		this.phone			=	phone;
		this.dob			=	dob;
		this.status			=	status;
		this.created		=	created;
		this.modified		=	modified;

	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the apiKey
	 */
	public String getApiKey() {
		return apiKey;
	}

	/**
	 * @param apiKey
	 *            the apiKey to set
	 */
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	/**
	 * @return the dob
	 */
	public String getDob() {
		return dob;
	}

	/**
	 * @param dob
	 *            the dob to set
	 */
	public void setDob(String dob) {
		this.dob = dob;
	}

	/**
	 * @return the status
	 */
	public byte getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(byte status) {
		this.status = status;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

}
