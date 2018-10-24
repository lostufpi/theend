package br.com.ufpi.systematicmap.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.caelum.vraptor.serialization.SkipSerialization;
import br.com.ufpi.systematicmap.model.enums.EvaluationStatusEnum;

@Entity
@Table(name = "evaluation")
public class Evaluation implements Serializable {

	private static final long serialVersionUID = 1;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Lob
	private String comment;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	@SkipSerialization
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "mapStudy_id")
	@SkipSerialization
	private MapStudy mapStudy;
	
	@ManyToOne
	@JoinColumn(name = "article_id")
	@SkipSerialization
	private Article article;
//	@SkipSerialization
	@ManyToMany
	@JoinTable(name="evaluations_inclusions", joinColumns={@JoinColumn(name="evaluation_id")}, inverseJoinColumns={@JoinColumn(name="inclusion_id")})
	private Set<InclusionCriteria> inclusionCriterias = new HashSet<>();
//	@SkipSerialization
	@ManyToMany
	@JoinTable(name="evaluations_exclusions", joinColumns={@JoinColumn(name="evaluation_id")}, inverseJoinColumns={@JoinColumn(name="exclusion_id")})
	private Set<ExclusionCriteria> exclusionCriterias = new HashSet<>();
	
	/**
	 * 
	 */
	public Evaluation() {
	}
	
	/**
	 * @param comment
	 * @param user
	 * @param mapStudy
	 * @param article
	 */
	public Evaluation(String comment, User user, MapStudy mapStudy, Article article) {
		super();
		this.comment = comment;
		this.user = user;
		this.mapStudy = mapStudy;
		this.article = article;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public MapStudy getMapStudy() {
		return mapStudy;
	}

	public void setMapStudy(MapStudy mapStudy) {
		this.mapStudy = mapStudy;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Set<InclusionCriteria> getInclusionCriterias() {
		return inclusionCriterias;
	}

	public void setInclusionCriterias(Set<InclusionCriteria> inclusionCriterias) {
		this.inclusionCriterias = inclusionCriterias;
	}

	public Set<ExclusionCriteria> getExclusionCriterias() {
		return exclusionCriterias;
	}

	public void setExclusionCriterias(Set<ExclusionCriteria> exclusionCriterias) {
		this.exclusionCriterias = exclusionCriterias;
	}
	
	public void addInclusion(InclusionCriteria inclusion) {
		getInclusionCriterias().add(inclusion);
		inclusion.getEvaluations().add(this);
	}
	
	public void addExclusion(ExclusionCriteria exclusion) {
		getExclusionCriterias().add(exclusion);
		exclusion.getEvaluations().add(this);
	}
	
	public String getClassification(){
		return getEvaluationStatus().getDescription();
	}
	
	public EvaluationStatusEnum getEvaluationStatus(){
		return inclusionCriterias.size() > 0 ? EvaluationStatusEnum.ACCEPTED : (exclusionCriterias.size() > 0 ? EvaluationStatusEnum.REJECTED : EvaluationStatusEnum.NOT_EVALUATED);
	}
	
	
}
