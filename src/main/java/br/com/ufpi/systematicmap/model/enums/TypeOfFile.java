package br.com.ufpi.systematicmap.model.enums;

import javax.inject.Named;

@Named("fileTypes")
public enum TypeOfFile {

	XLS(".xls"),
	CSV(".csv");

	private String description;

	private TypeOfFile(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description=description;
	}
	
}
