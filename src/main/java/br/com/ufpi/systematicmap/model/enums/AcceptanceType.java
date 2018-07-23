package br.com.ufpi.systematicmap.model.enums;

import javax.inject.Named;

@Named
public enum AcceptanceType {
	MY_ACCEPTACES("Meus Aceites."),
	ALL_ACCEPTANCES("Todos os aceites.");
	
	private String description;

	private AcceptanceType(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
