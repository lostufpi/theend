/**
 * 
 */
package br.com.ufpi.systematicmap.model;

import java.io.Serializable;

/**
 * @author Gleison Andrade
 *
 */
//@Entity
public class Comment implements Serializable{

	private static final long serialVersionUID = 1;
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