package br.com.ufpi.systematicmap.model;

import br.com.ufpi.systematicmap.model.enums.TypeMessage;

public class Mensagem {

	private String category;
	private String value;
	private TypeMessage type;

	public Mensagem(String category, String value, TypeMessage type) {
		this.category = category;
		this.value = value;
		this.type = type;
	}

	public String getType() {

		if (type == TypeMessage.SUCCESS)
			return "alert-success";
		if (type == TypeMessage.ERROR)
			return "alert-danger";
		if (type == TypeMessage.INFORMATION)
			return "alert-info";

		return null;

	}

	public String getClasse() {
		return "alert " + getType() + " alert-dismissible";
	}

	public String getCategory() {
		return category;
	}

	public String getValue() {
		return value;
	}

}
