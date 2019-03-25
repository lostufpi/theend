package br.com.ufpi.systematicmap.model.enums;

import javax.inject.Named;

@Named("fileTypes")
public enum TypeOfFile {

	XLS(".xls", "application/excel"),
	CSV(".csv", "text/csv");

	private String description;
	private String contentType;

	private TypeOfFile(String description, String contentType) {
		this.description = description;
		this.contentType = contentType;
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description=description;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}