/**
 * 
 */
package br.com.ufpi.systematicmap.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author gleys
 *
 */
@Entity
@Table(name="refinementparameters")
public class RefinementParameters implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Integer levenshtein = 0;
	private String regex;
	private Integer limiarTitle = 0;
	private Integer limiarAbstract = 0;
	private Integer limiarKeywords = 0;
	private Integer limiarTotal = 0;
	private Boolean filterAuthor = false;
	private Boolean filterAbstract = false;
	private Boolean filterLevenshtein = false;

	public RefinementParameters() {
		super();
		this.regex = regexSample().trim();
	}

	public RefinementParameters(Integer levenshtein, String regex, Integer limiarTitle, Integer limiarAbstract,
			Integer limiarKeywords, Integer limiarTotal, Boolean filterAuthor, Boolean filterAbstract,
			Boolean filterLevenshtein) {
		super();
		this.levenshtein = levenshtein;
		this.regex = regex;
		this.limiarTitle = limiarTitle;
		this.limiarAbstract = limiarAbstract;
		this.limiarKeywords = limiarKeywords;
		this.limiarTotal = limiarTotal;
		this.filterAuthor = filterAuthor;
		this.filterAbstract = filterAbstract;
		this.filterLevenshtein = filterLevenshtein;
	}

	private String regexSample() {
		return "automatico:(automat.*|semiautomati.*|semi-automati.*);" + 
				"web:(web|website|internet|www);" + 
				"usabilidade:(usability|usable);" + 
				"tecnica:(evalu.*|assess.*|measur.*|experiment.*|stud.*|test.*|method.*|techni.*|approach.*)";
	}

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
	 * @return the levenshtein
	 */
	public Integer getLevenshtein() {
		return levenshtein;
	}

	/**
	 * @param levenshtein the levenshtein to set
	 */
	public void setLevenshtein(Integer levenshtein) {
		this.levenshtein = levenshtein;
	}

	/**
	 * @return the regex
	 */
	public String getRegex() {
		return regex;
	}

	/**
	 * @param regex the regex to set
	 */
	public void setRegex(String regex) {
		this.regex = regex;
	}

	/**
	 * @return the limiarTitulo
	 */
	public Integer getLimiarTitle() {
		return limiarTitle;
	}

	/**
	 * @param limiarTitulo the limiarTitulo to set
	 */
	public void setLimiarTitle(Integer limiarTitle) {
		this.limiarTitle = limiarTitle;
	}

	/**
	 * @return the limiarAbstract
	 */
	public Integer getLimiarAbstract() {
		return limiarAbstract;
	}

	/**
	 * @param limiarAbstract the limiarAbstract to set
	 */
	public void setLimiarAbstract(Integer limiarAbstract) {
		this.limiarAbstract = limiarAbstract;
	}

	/**
	 * @return the limiarKeywords
	 */
	public Integer getLimiarKeywords() {
		return limiarKeywords;
	}

	/**
	 * @param limiarKeywords the limiarKeywords to set
	 */
	public void setLimiarKeywords(Integer limiarKeywords) {
		this.limiarKeywords = limiarKeywords;
	}

	/**
	 * @return the limiarTotal
	 */
	public Integer getLimiarTotal() {
		return limiarTotal;
	}

	/**
	 * @param limiarTotal the limiarTotal to set
	 */
	public void setLimiarTotal(Integer limiarTotal) {
		this.limiarTotal = limiarTotal;
	}

	/**
	 * @return the filterAuthor
	 */
	public Boolean getFilterAuthor() {
		return filterAuthor;
	}

	/**
	 * @param filterAuthor the filterAuthor to set
	 */
	public void setFilterAuthor(Boolean filterAuthor) {
		this.filterAuthor = filterAuthor;
	}

	/**
	 * @return the filterAbstract
	 */
	public Boolean getFilterAbstract() {
		return filterAbstract;
	}

	/**
	 * @param filterAbstract the filterAbstract to set
	 */
	public void setFilterAbstract(Boolean filterAbstract) {
		this.filterAbstract = filterAbstract;
	}

	/**
	 * @return the filterLevenshtein
	 */
	public Boolean getFilterLevenshtein() {
		return filterLevenshtein;
	}

	/**
	 * @param filterLevenshtein the filterLevenshtein to set
	 */
	public void setFilterLevenshtein(Boolean filterLevenshtein) {
		this.filterLevenshtein = filterLevenshtein;
	}
}