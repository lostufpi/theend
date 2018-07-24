/**
 * 
 */
package br.com.ufpi.systematicmap.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.caelum.vraptor.serialization.SkipSerialization;
import br.com.ufpi.systematicmap.model.enums.QuestionType;

/**
 * @author Gleison Andrade
 *
 */
@Entity
@Table(name="evaluation_extraction")
public class EvaluationExtraction implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	@SkipSerialization
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "article_id")
	@SkipSerialization
	private Article article;
	
	@Lob
	private String comment;
	
	@ManyToOne
	@JoinColumn(name = "alternative_id")
	private Alternative alternative;
	
	@ManyToOne
	@JoinColumn(name = "question_id")
	private Question question;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the article
	 */
	public Article getArticle() {
		return article;
	}

	/**
	 * @param article the article to set
	 */
	public void setArticle(Article article) {
		this.article = article;
	}

	/**
	 * @return the alternative
	 */
	public Alternative getAlternative() {
		return alternative;
	}

	/**
	 * @param alternative the alternative to set
	 */
	public void setAlternative(Alternative alternative) {
		this.alternative = alternative;
	}

	/**
	 * @return the question
	 */
	public Question getQuestion() {
		return question;
	}

	/**
	 * @param question the question to set
	 */
	public void setQuestion(Question question) {
		this.question = question;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((alternative == null) ? 0 : alternative.hashCode());
		result = prime * result + ((article == null) ? 0 : article.hashCode());
		result = prime * result
				+ ((question == null) ? 0 : question.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		EvaluationExtraction other = (EvaluationExtraction) obj;
		if (article == null) {
			if (other.article != null)
				return false;
		} else if (!article.equals(other.article))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		
		if (question == null) {
			if (other.question != null)
				return false;
		}else if(!question.getType().equals(QuestionType.MULT)){
			if (question.equals(other.question)){
				return true;
			}else{
				return false;
			}
		} //else if (!question.equals(other.question))
//			return false;	
		if (alternative == null) {
			if (other.alternative != null)
				return false;
		} else if (!alternative.equals(other.alternative))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EvaluationExtraction [id=" + id + ", user=" + user.getId()
				+ ", article=" + article.getId() + ", alternative=" + alternative.getValue()
				+ ", question=" + question.getName() + "]";
	}
	
	
	
}
