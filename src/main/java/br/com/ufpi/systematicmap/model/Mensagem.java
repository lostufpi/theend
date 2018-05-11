package br.com.ufpi.systematicmap.model;

import br.com.ufpi.systematicmap.model.enums.TipoMensagem;

public class Mensagem {

	private String category;
	private String value;
	private TipoMensagem type;

	public Mensagem(String category, String value, TipoMensagem type) {
		this.category = category;
		this.value = value;
		this.type = type;
	}

	public String getType() {

		if (type == TipoMensagem.SUCESSO)
			return "alert-success";
		if (type == TipoMensagem.ERRO)
			return "alert-danger";
		if (type == TipoMensagem.INFORMACAO)
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
