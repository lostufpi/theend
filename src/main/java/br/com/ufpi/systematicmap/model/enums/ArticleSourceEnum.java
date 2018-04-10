package br.com.ufpi.systematicmap.model.enums;

public enum ArticleSourceEnum {
	SCOPUS("Scopus"), 
	ENGINEERING_VILLAGE("Engineering Village"), 
	WEB_OF_SCIENCE("Web Of Science"), 
	PUBMED("PubMed"), 
	APA("American Psychological Association"),
	SPELL("Spell"),
	SCIELO("Scielo"),
	OTHER("Outros"), 
	MANUALLY("Manual");
	
	private String description;
	
	private ArticleSourceEnum(String description){
		this.description = description;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
