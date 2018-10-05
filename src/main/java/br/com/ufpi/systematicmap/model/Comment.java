/**
 * 
 */
package br.com.ufpi.systematicmap.model;

/**
 * @author Gleison Andrade
 *
 */
//@Entity
public class Comment {
	private User user;
	private String value;
	
	public Comment(User user, String value) {
		super();
		this.user = user;
		this.value = value;
	}

	public User getUser() {
		return user;
	}

	public String getValue() {
		return value;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setValue(String value) {
		this.value = value;
	}	
}