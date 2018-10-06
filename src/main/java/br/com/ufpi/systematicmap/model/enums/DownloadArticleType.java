package br.com.ufpi.systematicmap.model.enums;

public enum DownloadArticleType {
	MY_ACCEPTACES("Meus Aceites"),
	MY_REJECTED("Meus Rejeitados"),
	MY_ALL("Todas minhas avaliações"),
	ALL_ACCEPTANCES("Todos os aceites finais"),
	ALL_REJECTED("Todos os rejeitados finais"),
	ALL_EVALUATED("Todos as avaliações finais"),
	ALL("Todos os artigos");
	
	private String description;

	private DownloadArticleType(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String descritption) {
		this.description=descritption;
	}
	
}
