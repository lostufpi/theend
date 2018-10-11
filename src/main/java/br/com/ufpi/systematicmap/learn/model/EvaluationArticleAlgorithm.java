/**
 * 
 */
package br.com.ufpi.systematicmap.learn.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.caelum.vraptor.serialization.SkipSerialization;
import br.com.ufpi.systematicmap.model.Article;
import br.com.ufpi.systematicmap.model.enums.EvaluationStatusEnum;

/**
 * @author Gleison Andrade
 *
 */
@Entity
@Table(name = "evaluationarticlealgorithm")
public class EvaluationArticleAlgorithm implements Serializable{

	private static final long serialVersionUID = 1;
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "article_id")
	@SkipSerialization
	private Article article;
	
	@Enumerated(EnumType.STRING)
	private LearningAlgorithms algorithm;
	
	@Enumerated(EnumType.STRING)
	private EvaluationStatusEnum evaluation;

	/**
	 * 
	 */
	public EvaluationArticleAlgorithm() {
	}

	public EvaluationArticleAlgorithm(Article article, LearningAlgorithms algorithm, EvaluationStatusEnum evaluation) {
		super();
		this.article = article;
		this.algorithm = algorithm;
		this.evaluation = evaluation;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the article
	 */
	public Article getArticle() {
		return article;
	}

	/**
	 * @return the algorithm
	 */
	public LearningAlgorithms getAlgorithm() {
		return algorithm;
	}

	/**
	 * @return the evaluation
	 */
	public EvaluationStatusEnum getEvaluation() {
		return evaluation;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @param article the article to set
	 */
	public void setArticle(Article article) {
		this.article = article;
	}

	/**
	 * @param algorithm the algorithm to set
	 */
	public void setAlgorithm(LearningAlgorithms algorithm) {
		this.algorithm = algorithm;
	}

	/**
	 * @param evaluation the evaluation to set
	 */
	public void setEvaluation(EvaluationStatusEnum evaluation) {
		this.evaluation = evaluation;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((algorithm == null) ? 0 : algorithm.hashCode());
		result = prime * result + ((article == null) ? 0 : article.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EvaluationArticleAlgorithm other = (EvaluationArticleAlgorithm) obj;
		if (algorithm != other.algorithm)
			return false;
		if (article == null) {
			if (other.article != null)
				return false;
		} else if (!article.equals(other.article))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EvaluationArticleAlgorithm [id=" + id + ", article=" + article + ", algorithm=" + algorithm
				+ ", evaluation=" + evaluation + "]";
	}
	
}