package eu.planlos.anwesenheitsliste.model;

import javax.validation.constraints.NotNull;

public class LoginFormContainer {

	@NotNull
	private String username;
	
	@NotNull
	private String password;
	
	/**
	 * Standard constructor
	 */
	public LoginFormContainer() {
		super();
	}
	
	/**
	 * @param username
	 * @param password
	 */
	public LoginFormContainer(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	/**
	 * @return the loginName
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * @param loginName the loginName to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
		
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}	
}
