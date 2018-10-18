/**
 * 
 */
package br.com.ufpi.systematicmap.learn.model;

/**
 * @author Gleison Andrade
 *
 */
public enum ArticleStatusSelection {
	TEST("Teste"), TRANING("Treinamento"), NOT_USED("Não Utilizado");
	
	private String description;
	
	private ArticleStatusSelection(String description){
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
