/**
 * 
 */
package br.com.ufpi.systematicmap.learn.model;

/**
 * @author Gleison Andrade
 *
 */
public enum LearningAlgorithms {
	NAIVE_BAYES("Naive Bayes"),
	RANDOM_FLOREST("Random Florest"),
	COMPLEMENT_NAIVE_BAYES("Complement Naive Bayes");
	
	private String name;
	
	private LearningAlgorithms(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
